package pers.yufiria.customCommand.command;

import crypticlib.chat.BukkitMsgSender;
import crypticlib.command.BukkitCommand;
import crypticlib.command.BukkitSubcommand;
import crypticlib.command.CommandInfo;
import crypticlib.command.annotation.Command;
import crypticlib.command.annotation.Subcommand;
import crypticlib.perm.PermInfo;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.yufiria.customCommand.CustomCommandManager;
import pers.yufiria.customCommand.PluginMain;
import pers.yufiria.customCommand.config.Lang;

import java.util.List;
import java.util.Map;

@Command
public class PluginCommand extends BukkitCommand {

    public static final PluginCommand INSTANCE = new PluginCommand();

    private PluginCommand() {
        super(CommandInfo
                .builder("customcommand")
                .permission(new PermInfo("customcommand.command"))
                .build());
    }

    @Subcommand
    BukkitSubcommand reload = new BukkitSubcommand(
            CommandInfo
                    .builder("reload")
                    .permission(new PermInfo("customcommand.command.reload"))
                    .build()
    ) {
        @Override
        public void execute(@NotNull CommandSender commandSender, @NotNull List<String> args) {
            PluginMain.INSTANCE.reloadPlugin();
            BukkitMsgSender.INSTANCE.sendMsg(
                    commandSender,
                    Lang.commandReload.value(),
                    Map.of("<num>", CustomCommandManager.INSTANCE.getCustomCommands().size() + "")
            );
        }
    };

}
