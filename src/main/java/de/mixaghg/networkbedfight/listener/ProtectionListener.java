package de.mixaghg.networkbedfight.listener;

import de.mixaghg.networkbedfight.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;

public class ProtectionListener implements Listener {

    private final NetworkBedFightPlugin plugin;

    public ProtectionListener(NetworkBedFightPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (plugin.getGameManager().getGameState() != GameState.INGAME || !plugin.getGameManager().hasTeam(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent event) {
        event.setCancelled(true);
        event.setFoodLevel(20);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (plugin.getGameManager().getGameState() != GameState.INGAME || !plugin.getGameManager().hasTeam(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (plugin.getGameManager().getGameState() != GameState.INGAME || !plugin.getGameManager().hasTeam(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
