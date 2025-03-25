package pers.yufiria.customCommand.config;

import crypticlib.config.ConfigHandler;
import crypticlib.config.node.impl.bukkit.BooleanConfig;

@ConfigHandler(path = "config.yml")
public class Configs {

    public static final BooleanConfig debug = new BooleanConfig("debug", false);
    public static final BooleanConfig bStats = new BooleanConfig("bStats", true);

}
