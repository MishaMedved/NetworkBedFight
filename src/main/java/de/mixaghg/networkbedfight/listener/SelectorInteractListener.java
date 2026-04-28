package de.mixaghg.networkbedfight.listener;

import de.mixaghg.networkbedfight.*;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SelectorInteractListener implements Listener {

    private final NetworkBedFightPlugin plugin;

    public SelectorInteractListener(NetworkBedFightPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!ItemManager.isSelector(event.getItem())) return;

        event.setCancelled(true);
        TeamMenu.open(event.getPlayer(), plugin);
    }
}
