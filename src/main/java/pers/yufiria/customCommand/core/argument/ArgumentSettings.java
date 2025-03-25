package pers.yufiria.customCommand.core.argument;

import crypticlib.chat.BukkitMsgSender;
import crypticlib.util.IOHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import pers.yufiria.customCommand.core.argument.type.AbstractTypeSetting;
import pers.yufiria.customCommand.core.argument.type.TypeSettingsManager;
import pers.yufiria.customCommand.core.tab.CommandTabCompleter;
import pers.yufiria.customCommand.core.tab.TabCompleterManager;

import java.util.*;
import java.util.function.Function;

public class ArgumentSettings {

    private final @NotNull MaxArgument maxArgument;
    private final @NotNull MinArgument minArgument;
    private final @NotNull Map<Integer, AbstractTypeSetting> typeSettings;

    private ArgumentSettings(
        @NotNull MaxArgument maxArgument,
        @NotNull MinArgument minArgument,
        @NotNull Map<Integer, AbstractTypeSetting> typeSettings
    ) {
        this.maxArgument = maxArgument;
        this.minArgument = minArgument;
        this.typeSettings = typeSettings;
    }

    public boolean checkArguments(CommandSender sender, List<String> arguments) {
        if (!maxArgument.checkArgumentLength(arguments)) {
            BukkitMsgSender.INSTANCE.sendMsg(sender, maxArgument.hint());
            return false;
        }
        if (!minArgument.checkArgumentLength(arguments)) {
            BukkitMsgSender.INSTANCE.sendMsg(sender, minArgument.hint());
            return false;
        }
        for (int argumentIndex = 0; argumentIndex < arguments.size(); argumentIndex++) {
            String argument = arguments.get(argumentIndex);
            if (!typeSettings.containsKey(argumentIndex)) {
                continue;
            }
            AbstractTypeSetting typeSetting = typeSettings.get(argumentIndex);
            if (!typeSetting.checkArgument(argument)) {
                BukkitMsgSender.INSTANCE.sendMsg(sender, typeSetting.hint(), Map.of("%argument%", argument));
                return false;
            }
        }
        return true;
    }

    public static ArgumentSettings fromConfig(ConfigurationSection config) {
        MinArgument minArgument;
        if (!config.isConfigurationSection("min_argument")) {
            minArgument = new MinArgument(-1, null);
        } else {
            ConfigurationSection minArgumentConfig = Objects.requireNonNull(config.getConfigurationSection("min_argument"));
            int min = minArgumentConfig.getInt("min");
            String hint = minArgumentConfig.getString("hint");
            minArgument = new MinArgument(min, hint);
        }

        MaxArgument maxArgument;
        if (!config.isConfigurationSection("max_argument")) {
            maxArgument = new MaxArgument(-1, null);
        } else {
            ConfigurationSection maxArgumentConfig = Objects.requireNonNull(config.getConfigurationSection("max_argument"));
            int max = maxArgumentConfig.getInt("max");
            String hint = maxArgumentConfig.getString("hint");
            maxArgument = new MaxArgument(max, hint);
        }

        Map<Integer, AbstractTypeSetting> typeSettings = new HashMap<>();
        if (config.isConfigurationSection("type_settings")) {
            ConfigurationSection typeSettingsConfig = Objects.requireNonNull(config.getConfigurationSection("type_settings"));
            for (String key : typeSettingsConfig.getKeys(false)) {
                int index = Integer.parseInt(key) - 1;//为了符合非开发者的使用习惯
                ConfigurationSection typeSettingConfig = Objects.requireNonNull(typeSettingsConfig.getConfigurationSection(key));
                String typeId = typeSettingConfig.getString("type");
                Optional<AbstractTypeSetting> typeSettingOpt = TypeSettingsManager.INSTANCE.getTypeSetting(typeId, typeSettingConfig);
                if (!typeSettingOpt.isPresent()) {
                    IOHelper.info("&eUnknown argument type: " + typeId);
                    continue;
                }
                typeSettings.put(index, typeSettingOpt.get());
            }
        }
        return new ArgumentSettings(maxArgument, minArgument, typeSettings);
    }

}
