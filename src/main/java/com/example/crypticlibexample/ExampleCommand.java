package com.example.crypticlibexample;

import crypticlib.chat.BukkitMsgSender;
import crypticlib.command.BukkitCommand;
import crypticlib.command.BukkitSubcommand;
import crypticlib.command.CommandInfo;
import crypticlib.command.annotation.Command;
import crypticlib.command.annotation.Subcommand;
import crypticlib.perm.PermInfo;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Command
public class ExampleCommand extends BukkitCommand {

    public ExampleCommand() {
        super(new CommandInfo(
            "test",
            new PermInfo("test.test"),
            Arrays.asList("tt"),
            "测试命令",
            "用法: &r/test &7<...>"
            )
        );
    }

    @Subcommand
    BukkitSubcommand reload = new BukkitSubcommand(
        new CommandInfo(
            "reload",
            new PermInfo("test.test.reload"),
            Collections.emptyList(),
            "&7重载插件"
        )
    ) {
        @Override
        public void execute(@NotNull CommandSender sender, @NotNull List<String> args) {
            Example.INSTANCE.reloadPlugin();
            BukkitMsgSender.INSTANCE.sendMsg(sender, ExampleConfig.test.value());
        }
    };

    @Subcommand
    BukkitSubcommand test = new BukkitSubcommand("test2") {
        @Override
        public void execute(@NotNull CommandSender sender, @NotNull List<String> args) {
            BukkitMsgSender.INSTANCE.sendMsg(sender, ExampleConfig.test2.value().toString());
        }
    };


}
