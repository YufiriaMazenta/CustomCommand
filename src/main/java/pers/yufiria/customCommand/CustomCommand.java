package pers.yufiria.customCommand;

import crypticlib.action.Action;
import crypticlib.action.ActionCompiler;
import crypticlib.command.BukkitCommand;
import crypticlib.command.CommandInfo;
import crypticlib.perm.PermInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomCommand extends BukkitCommand {

    private final Action action;
    private static final Pattern ARG_PATTERN = Pattern.compile("<arg_(\\d+)>");

    public CustomCommand(String name, String permission, List<String> aliases, Action action) {
        super(CommandInfo.builder(name).permission(new PermInfo(permission)).aliases(aliases).build());
        this.action = action;
    }

    @Override
    public void execute(@NotNull CommandSender commandSender, @NotNull List<String> args) {
        Function<String, String> argPreprocessor = (arg) -> {
            Matcher matcher = ARG_PATTERN.matcher(arg);
            StringBuilder result = new StringBuilder();
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
        if (commandSender instanceof Player player) {
            this.action.run(player, PluginMain.INSTANCE, argPreprocessor);
        } else {
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
        return new CustomCommand(name, permission, aliases, action);
    }

}
