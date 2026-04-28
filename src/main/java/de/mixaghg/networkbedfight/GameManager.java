package de.mixaghg.networkbedfight;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GameManager {

    private final NetworkBedFightPlugin plugin;
    private GameState gameState;
    private Location team1Spawn;
    private Location team2Spawn;

    private final Map<UUID, TeamType> playerTeams = new HashMap<>();
    private final Set<UUID> frozenPlayers = new HashSet<>();

    public GameManager(NetworkBedFightPlugin plugin) {
        this.plugin = plugin;
        this.gameState = GameState.WAITING;
        loadSpawns();
    }

    public void setTeamSpawn(TeamType team, Location location) {
        if (team == TeamType.TEAM_1) {
            team1Spawn = location.clone();
            saveLocation("spawns.team1", location);
        } else {
            team2Spawn = location.clone();
            saveLocation("spawns.team2", location);
        }
        plugin.saveConfig();
    }

    public Location getTeamSpawn(TeamType team) {
        Location location = team == TeamType.TEAM_1 ? team1Spawn : team2Spawn;
        return location == null ? null : location.clone();
    }

    public boolean areSpawnsSet() { return team1Spawn != null && team2Spawn != null; }
    public GameState getGameState() { return gameState; }
    public TeamType getTeam(Player player) { return playerTeams.get(player.getUniqueId()); }
    public boolean hasTeam(Player player) { return playerTeams.containsKey(player.getUniqueId()); }
    public boolean isTeamTaken(TeamType team) { return playerTeams.containsValue(team); }
    public boolean isFrozen(Player player) { return frozenPlayers.contains(player.getUniqueId()); }

    public void assignTeam(Player player, TeamType team) {
        if (!areSpawnsSet()) {
            player.sendMessage("§cDie Team-Spawns wurden noch nicht gesetzt.");
            return;
        }
        if (hasTeam(player)) {
            player.sendMessage("§cDu hast bereits ein Team gewählt.");
            return;
        }
        if (isTeamTaken(team)) {
            player.sendMessage("§cDieses Team ist schon besetzt.");
            return;
        }

        playerTeams.put(player.getUniqueId(), team);
        player.closeInventory();
        player.teleport(getTeamSpawn(team));
        ItemManager.giveKit(player, team);
        frozenPlayers.add(player.getUniqueId());

        plugin.getScoreboardManager().updateAll();

        if (playerTeams.size() == 2 && gameState == GameState.WAITING) {
            startCountdown();
        }
    }

    public void removePlayer(Player player) {
        TeamType oldTeam = playerTeams.remove(player.getUniqueId());
        frozenPlayers.remove(player.getUniqueId());

        if (gameState == GameState.INGAME && oldTeam != null) {
            TeamType winner = oldTeam == TeamType.TEAM_1 ? TeamType.TEAM_2 : TeamType.TEAM_1;
            endGame(winner);
        } else {
            plugin.getScoreboardManager().updateAll();
        }
    }

    public void handleDeath(Player dead) {
        TeamType deadTeam = getTeam(dead);
        if (deadTeam == null) return;
        TeamType winner = deadTeam == TeamType.TEAM_1 ? TeamType.TEAM_2 : TeamType.TEAM_1;
        endGame(winner);
    }

    public void endGame(TeamType winner) {
        gameState = GameState.WAITING;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle("§a" + winner.getDisplayName() + " §agewinnt!", "§7Reset in 3 Sekunden", 10, 50, 10);
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
        }
        new BukkitRunnable() {
            @Override
            public void run() { resetMatch(); }
        }.runTaskLater(plugin, 60L);
    }

    public void resetMatch() {
        gameState = GameState.WAITING;
        playerTeams.clear();
        frozenPlayers.clear();

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.setHealth(20.0);
            player.setFoodLevel(20);
            player.setSaturation(20f);
            player.teleport(player.getWorld().getSpawnLocation());
            ItemManager.giveSelector(player, plugin);
        }
        plugin.getScoreboardManager().updateAll();
    }

    private void startCountdown() {
        gameState = GameState.STARTING;
        new BukkitRunnable() {
            private int seconds = 5;

            @Override
            public void run() {
                if (playerTeams.size() < 2) {
                    gameState = GameState.WAITING;
                    frozenPlayers.clear();
                    cancel();
                    return;
                }

                if (seconds > 0) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (hasTeam(player)) {
                            player.sendTitle("§e" + seconds, "§7Match startet", 0, 20, 0);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
                        }
                    }
                    seconds--;
                    return;
                }

                gameState = GameState.INGAME;
                frozenPlayers.clear();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (hasTeam(player)) {
                        player.sendTitle("§aGO!", "§7Kämpfe!", 0, 30, 10);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                    }
                }

                plugin.getScoreboardManager().updateAll();
                cancel();
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void loadSpawns() {
        team1Spawn = loadLocation("spawns.team1");
        team2Spawn = loadLocation("spawns.team2");
    }

    private void saveLocation(String path, Location location) {
        FileConfiguration config = plugin.getConfig();
        config.set(path + ".world", location.getWorld().getName());
        config.set(path + ".x", location.getX());
        config.set(path + ".y", location.getY());
        config.set(path + ".z", location.getZ());
        config.set(path + ".yaw", location.getYaw());
        config.set(path + ".pitch", location.getPitch());
    }

    private Location loadLocation(String path) {
        FileConfiguration config = plugin.getConfig();
        if (!config.contains(path + ".world")) return null;

        World world = Bukkit.getWorld(Objects.requireNonNull(config.getString(path + ".world")));
        if (world == null) return null;

        return new Location(
                world,
                config.getDouble(path + ".x"),
                config.getDouble(path + ".y"),
                config.getDouble(path + ".z"),
                (float) config.getDouble(path + ".yaw"),
                (float) config.getDouble(path + ".pitch")
        );
    }
}
