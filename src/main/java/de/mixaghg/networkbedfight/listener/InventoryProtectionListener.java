package de.mixaghg.networkbedfight.listener;

import de.mixaghg.networkbedfight.ItemManager;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class InventoryProtectionListener implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (ItemManager.isSelector(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (ItemManager.isSelector(event.getCurrentItem())) {
            event.setCancelled(true);
        }
    }
}
