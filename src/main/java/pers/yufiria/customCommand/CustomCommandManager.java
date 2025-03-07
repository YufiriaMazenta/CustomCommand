package pers.yufiria.customCommand;

import crypticlib.command.BukkitCommandManager;
import crypticlib.config.BukkitConfigWrapper;
import crypticlib.lifecycle.AutoTask;
import crypticlib.lifecycle.BukkitLifeCycleTask;
import crypticlib.lifecycle.LifeCycle;
import crypticlib.lifecycle.TaskRule;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@AutoTask(rules = {@TaskRule(lifeCycle = LifeCycle.ENABLE), @TaskRule(lifeCycle = LifeCycle.RELOAD)})
public enum CustomCommandManager implements BukkitLifeCycleTask {

    INSTANCE;

    private BukkitConfigWrapper commandConfig;
    private final Map<String, CustomCommand> customCommands = new ConcurrentHashMap<>();

    public void reloadCommands() {
        unregisterAllCustomCommands();
        if (commandConfig == null) {
            commandConfig = new BukkitConfigWrapper(PluginMain.INSTANCE, "commands.yml");
        }
        commandConfig.reloadConfig();
        YamlConfiguration config = commandConfig.config();
        for (String key : config.getKeys(false)) {
            try {
                ConfigurationSection commandConfig = config.getConfigurationSection(key);
                CustomCommand customCommand = CustomCommand.fromConfig(key, Objects.requireNonNull(commandConfig));
                customCommand.register(PluginMain.INSTANCE);
                customCommands.put(key, customCommand);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        BukkitCommandManager.INSTANCE.syncCommands();
    }

    public void unregisterAllCustomCommands() {
        customCommands.forEach((name, command) -> {
            BukkitCommandManager.INSTANCE.unregister(name);
        });
        customCommands.clear();
    }

    public Map<String, CustomCommand> getCustomCommands() {
        return Collections.unmodifiableMap(customCommands);
    }

    @Override
    public void run(Plugin plugin, LifeCycle lifeCycle) {
        try {
            reloadCommands();
            Bukkit.getOnlinePlayers().forEach(Player::updateCommands);
        } catch (Throwable throwable) {
            unregisterAllCustomCommands();
            BukkitCommandManager.INSTANCE.syncCommands();
            throwable.printStackTrace();
        }
    }
}
