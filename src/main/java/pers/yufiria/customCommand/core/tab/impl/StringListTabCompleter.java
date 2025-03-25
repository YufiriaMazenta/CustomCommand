package pers.yufiria.customCommand.core.tab.impl;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.yufiria.customCommand.core.tab.CommandTabCompleter;

import java.util.List;

public class StringListTabCompleter implements CommandTabCompleter {

    private final List<String> completeStrings;

    public StringListTabCompleter(List<String> completeStrings) {
        this.completeStrings = completeStrings;
    }

    @Override
    public @NotNull List<String> tabComplete(CommandSender sender, List<String> args) {
        return completeStrings;

    }
}
