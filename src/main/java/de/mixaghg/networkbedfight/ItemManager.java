package de.mixaghg.networkbedfight;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ItemManager {

    public static ItemStack createSelector() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§aTeam auswählen");
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static boolean isSelector(ItemStack item) {
        if (item == null || item.getType() != Material.NETHER_STAR || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasDisplayName() && meta.getDisplayName().equals("§aTeam auswählen");
    }

    public static void giveSelector(Player player, NetworkBedFightPlugin plugin) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setItem(plugin.getConfig().getInt("selector-slot", 4), createSelector());
    }

    public static void giveKit(Player player, TeamType team) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta stickMeta = stick.getItemMeta();
        if (stickMeta != null) {
            stickMeta.setDisplayName("§eKnockback Stick");
            stickMeta.addEnchant(Enchantment.KNOCKBACK, 2, true);
            stick.setItemMeta(stickMeta);
        }
        player.getInventory().setItem(0, stick);

        ItemStack wool = new ItemStack(team.getWoolMaterial(), 64);
        for (int i = 1; i <= 5; i++) player.getInventory().setItem(i, wool.clone());

        player.getInventory().setHelmet(leather(Material.LEATHER_HELMET, team));
        player.getInventory().setChestplate(leather(Material.LEATHER_CHESTPLATE, team));
        player.getInventory().setLeggings(leather(Material.LEATHER_LEGGINGS, team));
        player.getInventory().setBoots(leather(Material.LEATHER_BOOTS, team));
    }

    private static ItemStack leather(Material material, TeamType team) {
        ItemStack item = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        if (meta != null) {
            meta.setColor(team.getArmorColor());
            meta.setDisplayName(team.getDisplayName());
            item.setItemMeta(meta);
        }
        return item;
    }
}
