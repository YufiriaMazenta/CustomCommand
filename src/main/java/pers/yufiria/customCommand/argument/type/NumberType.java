package pers.yufiria.customCommand.argument.type;

import crypticlib.util.StringHelper;
import org.bukkit.configuration.ConfigurationSection;

public class NumberType extends AbstractTypeSetting {

    private final Double min, max;

    public NumberType(String hint, Double min, Double max) {
        super(hint);
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean checkArgument(String argument) {
        boolean isNumber = StringHelper.isNumber(argument);
        if (!isNumber) {
            return false;
        }
        double value = Double.parseDouble(argument);
        if (min != null) {
            if (max != null) {
                return value >= min && value <= max;
            } else {
                return value >= min;
            }
        } else {
            if (max != null) {
                return value <= max;
            } else {
                return true;
            }
        }
    }

    public static NumberType fromConfig(ConfigurationSection config) {
        Double min;
        if (config.contains("min")) {
            min = config.getDouble("min");
        } else {
            min = null;
        }
        Double max;
        if (config.contains("max")) {
            max = config.getDouble("max");
        } else {
            max = null;
        }
        String hint = config.getString("hint");
        return new NumberType(hint, min, max);
    }

}
