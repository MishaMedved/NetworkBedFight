package de.mixaghg.networkbedfight;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class BedFightScoreboard {

    private final NetworkBedFightPlugin plugin;
    private final File killsFile;
    private final YamlConfiguration killsConfig;

    public BedFightScoreboard(NetworkBedFightPlugin plugin) {
        this.plugin = plugin;
        this.killsFile = new File(plugin.getDataFolder(), "kills.yml");
        this.killsConfig = YamlConfiguration.loadConfiguration(killsFile);
    }

    public void addKill(Player player) {
        String path = "kills." + player.getUniqueId();
        int kills = killsConfig.getInt(path + ".amount", 0) + 1;
        killsConfig.set(path + ".name", player.getName());
        killsConfig.set(path + ".amount", kills);
        save();
        updateAll();
    }

    public void updateAll() {
        for (Player player : Bukkit.getOnlinePlayers()) update(player);
    }

    public void update(Player player) {
        org.bukkit.scoreboard.Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("bedfight", Criteria.DUMMY, "§cBedFight");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        TeamType team = plugin.getGameManager().getTeam(player);
        obj.getScore("§7────────").setScore(7);
        obj.getScore("§fState: §e" + plugin.getGameManager().getGameState().name()).setScore(6);
        obj.getScore("§fTeam: " + (team == null ? "§7Keins" : team.getDisplayName())).setScore(5);
        obj.getScore("§fOnline: §e" + Bukkit.getOnlinePlayers().size() + "§7/2").setScore(4);
        obj.getScore(" ").setScore(3);
        obj.getScore(getTopKillerLine()).setScore(2);
        obj.getScore("§7──────── ").setScore(1);

        player.setScoreboard(board);
    }

    private String getTopKillerLine() {
        if (killsConfig.getConfigurationSection("kills") == null) return "§eTop: §7Niemand";

        String bestName = "Niemand";
        int bestKills = 0;

        for (String uuid : Objects.requireNonNull(killsConfig.getConfigurationSection("kills")).getKeys(false)) {
            int kills = killsConfig.getInt("kills." + uuid + ".amount", 0);
            if (kills > bestKills) {
                bestKills = kills;
                bestName = killsConfig.getString("kills." + uuid + ".name", "Unbekannt");
            }
        }

        return "§eTop: §f" + bestName + " §7(" + bestKills + ")";
    }

    private void save() {
        try {
            killsConfig.save(killsFile);
        } catch (IOException e) {
            plugin.getLogger().warning("kills.yml konnte nicht gespeichert werden.");
        }
    }
}
