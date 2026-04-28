package de.mixaghg.networkbedfight.listener;

import de.mixaghg.networkbedfight.NetworkBedFightPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final NetworkBedFightPlugin plugin;

    public PlayerDeathListener(NetworkBedFightPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player dead = event.getEntity();
        Player killer = dead.getKiller();

        event.getDrops().clear();
        event.setDroppedExp(0);

        if (killer != null) {
            plugin.getScoreboardManager().addKill(killer);
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (dead.isOnline()) {
                dead.spigot().respawn();
                plugin.getGameManager().handleDeath(dead);
            }
        }, 2L);
    }
}
