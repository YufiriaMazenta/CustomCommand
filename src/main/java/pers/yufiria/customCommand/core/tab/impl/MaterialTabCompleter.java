package pers.yufiria.customCommand.core.tab.impl;

import crypticlib.command.CommandInvoker;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import pers.yufiria.customCommand.core.tab.CommandTabCompleter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MaterialTabCompleter implements CommandTabCompleter {

    INSTANCE;

    @Override
    public @NotNull List<String> tabComplete(CommandInvoker invoker, List<String> args) {
        return Arrays.stream(Material.values()).map(mat -> mat.getKey().toString()).collect(Collectors.toList());
    }

}
