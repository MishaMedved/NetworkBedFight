package de.mixaghg.networkbedfight;

import org.bukkit.Color;
import org.bukkit.Material;

public enum TeamType {
    TEAM_1("§9Team 1", Material.BLUE_WOOL, Color.BLUE),
    TEAM_2("§cTeam 2", Material.RED_WOOL, Color.RED);

    private final String displayName;
    private final Material woolMaterial;
    private final Color armorColor;

    TeamType(String displayName, Material woolMaterial, Color armorColor) {
        this.displayName = displayName;
        this.woolMaterial = woolMaterial;
        this.armorColor = armorColor;
    }

    public String getDisplayName() { return displayName; }
    public Material getWoolMaterial() { return woolMaterial; }
    public Color getArmorColor() { return armorColor; }
}
