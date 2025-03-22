package pers.yufiria.customCommand.argument.type.impl;

import crypticlib.util.IOHelper;
import crypticlib.util.MaterialHelper;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import pers.yufiria.customCommand.argument.type.AbstractTypeSetting;

import java.util.ArrayList;
import java.util.List;

public class MaterialType extends AbstractTypeSetting {

    private final List<Material> allowMaterials;

    public MaterialType(String hint, List<Material> allowMaterials) {
        super(hint);
        this.allowMaterials = allowMaterials;
    }

    @Override
    public boolean checkArgument(String argument) {
        Material material = MaterialHelper.matchMaterial(argument);
        if (material == null) {
            return false;
        }
        return allowMaterials.contains(material);
    }

    public static MaterialType fromConfig(ConfigurationSection config) {
        String hint = config.getString("hint");
        List<Material> allowMaterials = null;
        if (config.isList("allow_materials")) {
            allowMaterials = config.getStringList("allow_materials").stream().map(it -> {
                Material material = MaterialHelper.matchMaterial(it);
                if (material == null) {
                    IOHelper.info("&cUnknown material: " + it);
                }
                return material;
            }).toList();
        }
        return new MaterialType(hint, allowMaterials);
    }

}
