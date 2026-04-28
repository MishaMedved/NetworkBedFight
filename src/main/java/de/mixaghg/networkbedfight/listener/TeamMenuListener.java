package de.mixaghg.networkbedfight.listener;

import de.mixaghg.networkbedfight.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TeamMenuListener implements Listener {

    private final NetworkBedFightPlugin plugin;

    public TeamMenuListener(NetworkBedFightPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(TeamMenu.TITLE)) return;
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getCurrentItem() == null) return;

        Material type = event.getCurrentItem().getType();
        if (type == Material.GRAY_STAINED_GLASS_PANE) return;

        if (event.getSlot() == 2 && type == Material.BLUE_STAINED_GLASS_PANE) {
            plugin.getGameManager().assignTeam(player, TeamType.TEAM_1);
        } else if (event.getSlot() == 4 && type == Material.BLACK_STAINED_GLASS_PANE) {
            TeamMenu.sendToLobby(player, plugin);
        } else if (event.getSlot() == 6 && type == Material.RED_STAINED_GLASS_PANE) {
            plugin.getGameManager().assignTeam(player, TeamType.TEAM_2);
        }
    }
}
