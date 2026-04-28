package de.mixaghg.networkbedfight.listener;

import de.mixaghg.networkbedfight.ItemManager;
import de.mixaghg.networkbedfight.NetworkBedFightPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final NetworkBedFightPlugin plugin;

    public PlayerJoinListener(NetworkBedFightPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (Bukkit.getOnlinePlayers().size() > plugin.getConfig().getInt("max-players", 2)) {
            event.getPlayer().kickPlayer("§cDas BedFight-Match ist voll.");
            return;
        }

        event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
        event.getPlayer().setGameMode(GameMode.SURVIVAL);
        event.getPlayer().setHealth(20);
        event.getPlayer().setFoodLevel(20);
        event.getPlayer().setSaturation(20f);
        ItemManager.giveSelector(event.getPlayer(), plugin);
        plugin.getScoreboardManager().updateAll();
    }
}
