package pers.yufiria.customCommand;

import crypticlib.action.Action;
import crypticlib.action.ActionCompiler;
import crypticlib.command.BukkitCommand;
import crypticlib.command.CommandInfo;
import crypticlib.perm.PermInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class CustomCommand extends BukkitCommand {

    private final Action action;

    public CustomCommand(String name, String permission, List<String> aliases, Action action) {
        super(CommandInfo.builder(name).permission(new PermInfo(permission)).aliases(aliases).build());
        this.action = action;
    }

    @Override
    public void execute(@NotNull CommandSender commandSender, @NotNull List<String> args) {
        if (commandSender instanceof Player player) {
            this.action.run(player, PluginMain.INSTANCE);
        } else {
            this.action.run(null, PluginMain.INSTANCE);
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
