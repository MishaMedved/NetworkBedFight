package de.mixaghg.networkbedfight;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamMenu {

    public static final String TITLE = "§8Team wählen";

    public static void open(Player player, NetworkBedFightPlugin plugin) {
        Inventory inv = Bukkit.createInventory(null, 9, TITLE);
        inv.setItem(2, teamItem(plugin, TeamType.TEAM_1));
        inv.setItem(4, item(Material.BLACK_STAINED_GLASS_PANE, "§8Zurück zur Lobby"));
        inv.setItem(6, teamItem(plugin, TeamType.TEAM_2));
        player.openInventory(inv);
    }

    private static ItemStack teamItem(NetworkBedFightPlugin plugin, TeamType team) {
        boolean taken = plugin.getGameManager().isTeamTaken(team);
        Material material = team == TeamType.TEAM_1
                ? (taken ? Material.GRAY_STAINED_GLASS_PANE : Material.BLUE_STAINED_GLASS_PANE)
                : (taken ? Material.GRAY_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);

        return item(material, taken ? "§7N.A" : team.getDisplayName());
    }

    private static ItemStack item(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static void sendToLobby(Player player, NetworkBedFightPlugin plugin) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(plugin.getConfig().getString("lobby-server-name", "lobby"));
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }
}
