package pers.yufiria.customCommand;

import crypticlib.BukkitPlugin;
import crypticlib.action.ActionCompiler;
import pers.yufiria.customCommand.action.ActionPack;

public class PluginMain extends BukkitPlugin {

    public static PluginMain INSTANCE;

    public PluginMain() { INSTANCE = this; }

    @Override
    public void load() {
        ActionCompiler.INSTANCE.regAction("action-pack", ActionPack::new);
    }

}