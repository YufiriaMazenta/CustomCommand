package pers.yufiria.customCommand.core;

import crypticlib.action.Action;
import crypticlib.action.ActionCompiler;
import crypticlib.chat.BukkitMsgSender;
import crypticlib.command.CommandInfo;
import crypticlib.command.CommandInvoker;
import crypticlib.command.CommandTree;
import crypticlib.perm.PermInfo;
import crypticlib.util.IOHelper;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.yufiria.customCommand.PluginMain;
import pers.yufiria.customCommand.core.argument.ArgumentSettings;
import pers.yufiria.customCommand.core.tab.CommandTabCompleter;
import pers.yufiria.customCommand.core.tab.TabCompleterManager;
import pers.yufiria.customCommand.util.EntityLookUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomCommand extends CommandTree {

    private static final Pattern ARG_PATTERN = Pattern.compile("<arg_(\\d+)>");

    private final Action action;
    private final Integer cooldownTick;
    private final @Nullable String cooldownMessage;
    private final Map<UUID, Long> playerLastExecuteMap = new ConcurrentHashMap<>();
    private final @Nullable ArgumentSettings argumentSettings;
    private final @NotNull Map<Integer, CommandTabCompleter> tabCompleterMap;
    private Long consoleLastExecuteTime = 0L;

    public CustomCommand(
        String name,
        String permission,
        List<String> aliases,
        Action action,
        Integer cooldownTick,
        @Nullable String cooldownMessage,
        @Nullable ArgumentSettings argumentSettings,
        @NotNull Map<Integer, CommandTabCompleter> tabCompleterMap
    ) {
        super(CommandInfo.builder(name).permission(new PermInfo(permission)).aliases(aliases).build());
        this.action = action;
        this.cooldownTick = cooldownTick;
        this.cooldownMessage = cooldownMessage;
        this.argumentSettings = argumentSettings;
        this.tabCompleterMap = tabCompleterMap;
    }

    @Override
    public void execute(@NotNull CommandInvoker commandInvoker, @NotNull List<String> args) {
        Function<String, String> argPreprocessor = (arg) -> {
            Matcher matcher = ARG_PATTERN.matcher(arg);
            StringBuffer result = new StringBuffer();
            while (matcher.find()) {
                //为了方便非开发者理解,索引从1开始
                int argIndex = Integer.parseInt(matcher.group(1)) - 1;

                // 判断 index 是否在替换列表的范围内
                String replacement;
                if (argIndex >= 0 && argIndex < args.size()) {
                    replacement = args.get(argIndex);
                } else {
                    replacement = "";  // 如果超出范围，用空字符串替换
                }

                // 替换为对应的字符串
                matcher.appendReplacement(result, replacement);
            }

            matcher.appendTail(result);
            String resultStr = result.toString().trim();

            //替换参数列表中正在查看的实体的信息
            if (commandInvoker.isPlayer()) {
                Optional<Entity> lookingEntityOpt = EntityLookUtil.getLookingEntity((Player) commandInvoker.asPlayer().getPlatformPlayer());
                if (lookingEntityOpt.isPresent()) {
                    Entity entity = lookingEntityOpt.get();
                    resultStr = resultStr.replace("%looking_entity_name%", entity.getName());
                    resultStr = resultStr.replace("%looking_entity_uuid%", entity.getUniqueId().toString());
                } else {
                    resultStr = resultStr.replace("%looking_entity_name%", "Unknown");
                    resultStr = resultStr.replace("%looking_entity_uuid%", "Unknown");
                }
            }
            return resultStr;
        };

        if (argumentSettings != null) {
            if (!(argumentSettings.checkArguments(commandInvoker, args))) {
                return;
            }
        }

        long current = System.currentTimeMillis();
        if (commandInvoker.isPlayer()) {
            Player player = (Player) commandInvoker.asPlayer().getPlatformPlayer();
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
                commandInvoker.sendMsg(cooldownMessage);
                return;
            }
            consoleLastExecuteTime = current;
            this.action.run(null, PluginMain.INSTANCE, argPreprocessor);
        }
    }

    @Override
    public @Nullable List<String> tab(@NotNull CommandInvoker sender, @NotNull List<String> args) {
        CommandTabCompleter commandTabCompleter = tabCompleterMap.get(args.size());
        if (commandTabCompleter == null) {
            return Collections.emptyList();
        }
        return commandTabCompleter.tabComplete(sender, args);
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
        Map<Integer, CommandTabCompleter> tabCompleterMap = new HashMap<>();
        if (config.isConfigurationSection("tab_completer")) {
            ConfigurationSection tabCompleterConfig = Objects.requireNonNull(config.getConfigurationSection("tab_completer"));
            for (String key : tabCompleterConfig.getKeys(false)) {
                int index = Integer.parseInt(key);
                ConfigurationSection tabConfig = Objects.requireNonNull(tabCompleterConfig.getConfigurationSection(key));
                String tabCompleterType = tabConfig.getString("type");
                Optional<Function<ConfigurationSection, CommandTabCompleter>> commandTabCompleterCreator = TabCompleterManager.INSTANCE.getCommandTabCompleterCreator(tabCompleterType);
                if (!commandTabCompleterCreator.isPresent()) {
                    IOHelper.info("&eUnknown argument tab completer type: " + tabCompleterType);
                    continue;
                }
                tabCompleterMap.put(index, commandTabCompleterCreator.get().apply(tabConfig));
            }
        }
        return new CustomCommand(name, permission, aliases, action, cooldownTick, cooldownMsg, argumentSettings, tabCompleterMap);
    }

}
