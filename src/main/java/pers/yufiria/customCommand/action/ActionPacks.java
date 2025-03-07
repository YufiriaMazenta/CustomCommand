package pers.yufiria.customCommand.action;

import crypticlib.action.Action;
import crypticlib.action.ActionCompiler;
import crypticlib.config.BukkitConfigWrapper;
import crypticlib.lifecycle.AutoTask;
import crypticlib.lifecycle.BukkitLifeCycleTask;
import crypticlib.lifecycle.LifeCycle;
import crypticlib.lifecycle.TaskRule;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import pers.yufiria.customCommand.PluginMain;

import java.util.LinkedHashMap;
import java.util.Map;

@AutoTask(
    rules = {
        @TaskRule(
            lifeCycle = LifeCycle.ENABLE, priority = -1
        ),
        @TaskRule(
            lifeCycle = LifeCycle.RELOAD, priority = -1
        )
    }
)
public enum ActionPacks implements BukkitLifeCycleTask {

    INSTANCE;

    private BukkitConfigWrapper actionPacksConfig;
    private final Map<String, Action> actionPackMap = new LinkedHashMap<>();

    public Action getActionPack(String pack) {
        return actionPackMap.get(pack);
    }

    @Override
    public void run(Plugin plugin, LifeCycle lifeCycle) {
        actionPackMap.clear();
        if (actionPacksConfig == null) {
            actionPacksConfig = new BukkitConfigWrapper(PluginMain.INSTANCE, "action_packs.yml");
        }
        actionPacksConfig.reloadConfig();
        YamlConfiguration config = actionPacksConfig.config();
        for (String key : config.getKeys(false)) {
            if (config.isList(key)) {
                actionPackMap.put(key, ActionCompiler.INSTANCE.compile(config.getStringList(key)));
            } else {
                actionPackMap.put(key, ActionCompiler.INSTANCE.compile(config.getString(key)));
            }
        }
    }

}
