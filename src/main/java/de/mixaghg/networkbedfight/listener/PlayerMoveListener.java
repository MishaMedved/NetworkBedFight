package de.mixaghg.networkbedfight.listener;

import de.mixaghg.networkbedfight.NetworkBedFightPlugin;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private final NetworkBedFightPlugin plugin;

    public PlayerMoveListener(NetworkBedFightPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!plugin.getGameManager().isFrozen(event.getPlayer())) return;
        if (event.getTo() == null) return;
        if (event.getFrom().getBlockX() == event.getTo().getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) return;

        event.setTo(event.getFrom());
    }
}
