package pers.yufiria.customCommand;

import crypticlib.action.Action;
import crypticlib.action.ActionCompiler;
import crypticlib.chat.BukkitMsgSender;
import crypticlib.command.BukkitCommand;
import crypticlib.command.CommandInfo;
import crypticlib.perm.PermInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.yufiria.customCommand.argument.ArgumentSettings;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomCommand extends BukkitCommand {

    private static final Pattern ARG_PATTERN = Pattern.compile("<arg_(\\d+)>");

    private final Action action;
    private final Integer cooldownTick;
    private final @Nullable String cooldownMessage;
    private final Map<UUID, Long> playerLastExecuteMap = new ConcurrentHashMap<>();
    private final @Nullable ArgumentSettings argumentSettings;
    private Long consoleLastExecuteTime = 0L;

    public CustomCommand(String name, String permission, List<String> aliases, Action action, Integer cooldownTick, @Nullable String cooldownMessage, @Nullable ArgumentSettings argumentSettings) {
        super(CommandInfo.builder(name).permission(new PermInfo(permission)).aliases(aliases).build());
        this.action = action;
        this.cooldownTick = cooldownTick;
        this.cooldownMessage = cooldownMessage;
        this.argumentSettings = argumentSettings;
    }

    @Override
    public void execute(@NotNull CommandSender commandSender, @NotNull List<String> args) {
        Function<String, String> argPreprocessor = (arg) -> {
            Matcher matcher = ARG_PATTERN.matcher(arg);
            StringBuffer result = new StringBuffer();
            while (matcher.find()) {
                int index = Integer.parseInt(matcher.group(1));

                // 判断 index 是否在替换列表的范围内
                String replacement;
                if (index >= 0 && index < args.size()) {
                    replacement = args.get(index);
                } else {
                    replacement = "";  // 如果超出范围，用空字符串替换
                }

                // 替换为对应的字符串
                matcher.appendReplacement(result, replacement);
            }

            matcher.appendTail(result);
            return result.toString().trim();
        };

        if (argumentSettings != null) {
            if (!(argumentSettings.checkArguments(commandSender, args))) {
                return;
            }
        }

        long current = System.currentTimeMillis();
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            UUID playerUniqueId = player.getUniqueId();
            if (playerLastExecuteMap.containsKey(playerUniqueId)) {
                Long last = playerLastExecuteMap.get(playerUniqueId);
                if ((current - last) / 50 < cooldownTick) {
                    BukkitMsgSender.INSTANCE.sendMsg(player, cooldownMessage);
                    return;
                }
                playerLastExecuteMap.put(playerUniqueId, current);
            }
            this.action.run(player, PluginMain.INSTANCE, argPreprocessor);
        } else {
            if ((current - consoleLastExecuteTime) / 50 < cooldownTick) {
                BukkitMsgSender.INSTANCE.sendMsg(commandSender, cooldownMessage);
                return;
            }
            consoleLastExecuteTime = current;
            this.action.run(null, PluginMain.INSTANCE, argPreprocessor);
        }
    }

    public static CustomCommand fromConfig(@NotNull String name, @NotNull ConfigurationSection config)  {
        Objects.requireNonNull(name, "Command's name cannot be null");
        Objects.requireNonNull(config, "Command's config cannot be null");
        List<String> aliases = config.getStringList("aliases");
        String permission = config.getString("permission");
        List<String> actionStrList = config.getStringList("actions");
        Action action = ActionCompiler.INSTANCE.compile(actionStrList);
        int cooldownTick = config.getInt("cooldown", 0);
        String cooldownMsg = config.getString("cooldown_message");
        ArgumentSettings argumentSettings;
        if (config.isConfigurationSection("argument_settings")) {
            argumentSettings = ArgumentSettings.fromConfig(Objects.requireNonNull(config.getConfigurationSection("argument_settings")));
        } else {
            argumentSettings = null;
        }
        return new CustomCommand(name, permission, aliases, action, cooldownTick, cooldownMsg, argumentSettings);
    }

}
