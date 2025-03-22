package pers.yufiria.customCommand.argument.type;

import org.bukkit.configuration.ConfigurationSection;
import pers.yufiria.customCommand.argument.type.impl.MaterialType;
import pers.yufiria.customCommand.argument.type.impl.NumberType;
import pers.yufiria.customCommand.argument.type.impl.PlayerType;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public enum TypeSettingsManager {

    INSTANCE;

    TypeSettingsManager() {
        registerTypeSetting("number", NumberType::fromConfig);
        registerTypeSetting("player", PlayerType::fromConfig);
        registerTypeSetting("material", MaterialType::fromConfig);
    }

    private final Map<String, Function<ConfigurationSection, AbstractTypeSetting>> typeSettingFunctionMap = new ConcurrentHashMap<>();

    public void registerTypeSetting(String typeId, Function<ConfigurationSection, AbstractTypeSetting> typeSettingFunction) {
        typeSettingFunctionMap.put(typeId, typeSettingFunction);
    }

    public Optional<AbstractTypeSetting> getTypeSetting(String typeId, ConfigurationSection config) {
        if (!typeSettingFunctionMap.containsKey(typeId)) {
            return Optional.empty();
        }
        return Optional.ofNullable(typeSettingFunctionMap.get(typeId).apply(config));
    }

}
