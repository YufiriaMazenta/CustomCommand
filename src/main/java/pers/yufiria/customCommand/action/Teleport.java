package pers.yufiria.customCommand.action;

import crypticlib.CrypticLibBukkit;
import crypticlib.action.BaseAction;
import crypticlib.util.MapHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class Teleport extends BaseAction {

    private final Location location;

    public Teleport(String arg) {
        Map<String, String> argMap = MapHelper.keyValueText2Map(arg);
        int x = Integer.parseInt(argMap.get("x"));
        int y = Integer.parseInt(argMap.get("y"));
        int z = Integer.parseInt(argMap.get("z"));
        World world = Bukkit.getWorld(argMap.get("world"));
        this.location = new Location(world, x, y, z);
    }

    @Override
    public String toActionStr() {
        Map<String, String> map = new HashMap<>();
        map.put("x", String.valueOf(location.getX()));
        map.put("y", String.valueOf(location.getY()));
        map.put("z", String.valueOf(location.getZ()));
        map.put("world", location.getWorld().getName());
        return "teleport " + MapHelper.map2KeyValueText(map);
    }

    @Override
    public void run(@Nullable Player player, @NotNull Plugin plugin, @Nullable Function<String, String> function) {
        if (location != null) {
            if (player != null) {
                CrypticLibBukkit.platform().teleportEntity(player, location);
            }
        }
        runNext(player, plugin, function);
    }

}
