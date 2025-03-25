package pers.yufiria.customCommand.core.tab.impl;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import pers.yufiria.customCommand.core.tab.CommandTabCompleter;

import java.util.List;
import java.util.stream.Collectors;

public enum PlayerNameTabCompleter implements CommandTabCompleter {

    INSTANCE;

    @Override
    public @NotNull List<String> tabComplete(CommandSender sender, List<String> args) {
        return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
    }

}
