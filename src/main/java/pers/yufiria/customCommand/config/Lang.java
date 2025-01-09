package pers.yufiria.customCommand.config;

import crypticlib.config.ConfigHandler;
import crypticlib.config.node.impl.bukkit.StringConfig;

@ConfigHandler(path = "lang.yml")
public class Lang {

    public static final StringConfig commandReload = new StringConfig("command.reload", "&8[&3Custom&bCommand&8] &a插件重载完毕,共加载了<num>个命令");

}
