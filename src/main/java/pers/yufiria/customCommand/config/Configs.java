package pers.yufiria.customCommand.config;

import crypticlib.config.ConfigHandler;
import crypticlib.config.node.impl.bukkit.BooleanConfig;
import crypticlib.config.node.impl.bukkit.DoubleConfig;

@ConfigHandler(path = "config.yml")
public class Configs {

    public static final BooleanConfig debug = new BooleanConfig("debug", false);
    public static final BooleanConfig bStats = new BooleanConfig("bStats", true);
    public static final DoubleConfig lookingEntityMaxSearchDistance = new DoubleConfig("looking_entity.max_search_distance", 6.0);

}
