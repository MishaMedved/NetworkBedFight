package de.mixaghg.networkbedfight.listener;

import de.mixaghg.networkbedfight.NetworkBedFightPlugin;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final NetworkBedFightPlugin plugin;

    public PlayerQuitListener(NetworkBedFightPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getGameManager().removePlayer(event.getPlayer());
    }
}
