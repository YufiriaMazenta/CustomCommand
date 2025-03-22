package pers.yufiria.customCommand.argument.type.impl;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import pers.yufiria.customCommand.argument.type.AbstractTypeSetting;

public class PlayerType extends AbstractTypeSetting {

    public PlayerType(String hint) {
        super(hint);
    }

    @Override
    public boolean checkArgument(String argument) {
        Player player = Bukkit.getPlayer(argument);
        return player != null && player.isOnline();
    }

    public static PlayerType fromConfig(ConfigurationSection config) {
        String hint = config.getString("hint");
        return new PlayerType(hint);
    }

}
