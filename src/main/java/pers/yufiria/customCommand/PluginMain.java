package pers.yufiria.customCommand;

import crypticlib.BukkitPlugin;
import crypticlib.CrypticLib;
import crypticlib.CrypticLibBukkit;
import crypticlib.action.ActionCompiler;
import pers.yufiria.customCommand.action.ActionPack;
import pers.yufiria.customCommand.action.Teleport;
import pers.yufiria.customCommand.config.Configs;

public class PluginMain extends BukkitPlugin {

    public static PluginMain INSTANCE;

    public PluginMain() { INSTANCE = this; }

    @Override
    public void load() {
        ActionCompiler.INSTANCE.regAction(ActionPack.ACTION_PACK_ACTION_KEY, ActionPack::new);
        ActionCompiler.INSTANCE.regAction("teleport", Teleport::new);
    }

    @Override
    public void enable() {
        CrypticLib.setDebug(Configs.debug.value());
        if (Configs.bStats.value()) {
            //TODO 由于插件还未确定名字,暂时没有bstats信息
        }
    }

    @Override
    public void reload() {
        CrypticLib.setDebug(Configs.debug.value());
    }

}