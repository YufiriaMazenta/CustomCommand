package pers.yufiria.customCommand.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import pers.yufiria.customCommand.config.Configs;

import java.util.Optional;

public class EntityLookUtil {

    /**
     * 获取玩家正在注视的实体
     * @param player 要检测的玩家
     * @return 玩家正在注视的实体，若没有则返回 null
     */
    public static Optional<Entity> getLookingEntity(Player player) {
        RayTraceResult rayTraceResult = player.getWorld().rayTraceEntities(
            player.getEyeLocation(),
            player.getEyeLocation().getDirection(),
            Configs.lookingEntityMaxSearchDistance.value(),
            0.0,
            entity -> !entity.equals(player)
        );
        return Optional.ofNullable(rayTraceResult != null ? rayTraceResult.getHitEntity() : null);
    }

}
