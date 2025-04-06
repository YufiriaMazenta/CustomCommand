package pers.yufiria.customCommand.command;

import crypticlib.command.*;
import crypticlib.command.annotation.Command;
import crypticlib.command.annotation.Subcommand;
import crypticlib.perm.PermInfo;
import org.jetbrains.annotations.NotNull;
import pers.yufiria.customCommand.core.CustomCommandManager;
import pers.yufiria.customCommand.PluginMain;
import pers.yufiria.customCommand.config.Lang;

import java.util.Collections;
import java.util.List;

@Command
public class PluginCommand extends CommandTree {

    public static final PluginCommand INSTANCE = new PluginCommand();

    private PluginCommand() {
        super(CommandInfo
                .builder("customcommand")
                .permission(new PermInfo("customcommand.command"))
                .build());
    }

    @Subcommand
    CommandNode reload = new CommandNode(
            CommandInfo
                    .builder("reload")
                    .permission(new PermInfo("customcommand.command.reload"))
                    .build()
    ) {
        @Override
        public void execute(@NotNull CommandInvoker commandInvoker, @NotNull List<String> args) {
            PluginMain.INSTANCE.reloadPlugin();
            commandInvoker.sendMsg(
                Lang.commandReload.value(),
                Collections.singletonMap("<num>", CustomCommandManager.INSTANCE.getCustomCommands().size() + "")
            );
        }
    };

}
