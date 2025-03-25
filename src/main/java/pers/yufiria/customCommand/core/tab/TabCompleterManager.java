package pers.yufiria.customCommand.core.tab;

import org.bukkit.configuration.ConfigurationSection;
import pers.yufiria.customCommand.core.tab.impl.MaterialTabCompleter;
import pers.yufiria.customCommand.core.tab.impl.PlayerNameTabCompleter;
import pers.yufiria.customCommand.core.tab.impl.StringListTabCompleter;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public enum TabCompleterManager {

    INSTANCE;

    private final Map<String, Function<ConfigurationSection, CommandTabCompleter>> commandTabCompleterCreatorMap = new ConcurrentHashMap<>();

    TabCompleterManager() {
        regCommandTabCompleterCreator("list", config -> {
            if (config.isList("values")) {
                return new StringListTabCompleter(config.getStringList("values"));
            } else {
                return new StringListTabCompleter(new ArrayList<>());
            }
        });
        regCommandTabCompleterCreator("material", config -> MaterialTabCompleter.INSTANCE);
        regCommandTabCompleterCreator("player_name", config -> PlayerNameTabCompleter.INSTANCE);
    }

    public void regCommandTabCompleterCreator(String completerType, Function<ConfigurationSection, CommandTabCompleter> creator) {
        commandTabCompleterCreatorMap.put(completerType, creator);
    }

    public Function<ConfigurationSection, CommandTabCompleter> removeCommandTabCompleterCreator(String tabCompleterType) {
        return commandTabCompleterCreatorMap.remove(tabCompleterType);
    }

    public Optional<Function<ConfigurationSection, CommandTabCompleter>> getCommandTabCompleterCreator(String tabCompleterType) {
        return Optional.ofNullable(commandTabCompleterCreatorMap.get(tabCompleterType));
    }

}
