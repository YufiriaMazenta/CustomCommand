package pers.yufiria.customCommand.action;

import crypticlib.action.Action;
import crypticlib.action.BaseAction;
import crypticlib.util.IOHelper;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ActionPack extends BaseAction {

    private final Action action;

    public ActionPack(String packId) {
        this.action = ActionPacks.INSTANCE.getActionPack(packId);
        if (action == null) {
            IOHelper.info("&eWARN: Unknown action pack: " + packId);
        }
    }

    @Override
    public String toActionStr() {
        return "action_pack";
    }

    @Override
    public void run(@Nullable Player player, @NotNull Plugin plugin, @Nullable Function<String, String> function) {
        if (action != null) {
            action.run(player, plugin, function);
        }
        runNext(player, plugin, function);
    }
}
