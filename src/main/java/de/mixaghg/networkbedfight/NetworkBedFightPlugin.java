package de.mixaghg.networkbedfight;

import de.mixaghg.networkbedfight.command.BedFightResetCommand;
import de.mixaghg.networkbedfight.command.SetCommand;
import de.mixaghg.networkbedfight.listener.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class NetworkBedFightPlugin extends JavaPlugin {

    private GameManager gameManager;
    private BedFightScoreboard scoreboardManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.gameManager = new GameManager(this);
        this.scoreboardManager = new BedFightScoreboard(this);

        Objects.requireNonNull(getCommand("set")).setExecutor(new SetCommand(this));
        Objects.requireNonNull(getCommand("bedfightreset")).setExecutor(new BedFightResetCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new SelectorInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new TeamMenuListener(this), this);
        getServer().getPluginManager().registerEvents(new ProtectionListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new MobSpawnListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryProtectionListener(), this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getLogger().info("NetworkBedFight aktiviert.");
    }

    @Override
    public void onDisable() {
        getLogger().info("NetworkBedFight deaktiviert.");
    }

    public GameManager getGameManager() { return gameManager; }
    public BedFightScoreboard getScoreboardManager() { return scoreboardManager; }
}
