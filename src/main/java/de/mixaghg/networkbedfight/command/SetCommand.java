package de.mixaghg.networkbedfight.command;

import de.mixaghg.networkbedfight.NetworkBedFightPlugin;
import de.mixaghg.networkbedfight.TeamType;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class SetCommand implements CommandExecutor {

    private final NetworkBedFightPlugin plugin;

    public SetCommand(NetworkBedFightPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Nur Spieler können diesen Command ausführen.");
            return true;
        }
        if (!player.isOp()) {
            player.sendMessage("§cKeine Rechte.");
            return true;
        }
        if (args.length != 1) {
            player.sendMessage("§cBenutzung: /set <team1|team2>");
            return true;
        }

        if (args[0].equalsIgnoreCase("team1")) {
            plugin.getGameManager().setTeamSpawn(TeamType.TEAM_1, player.getLocation());
            player.sendMessage("§aTeam 1 Spawn gesetzt.");
            return true;
        }

        if (args[0].equalsIgnoreCase("team2")) {
            plugin.getGameManager().setTeamSpawn(TeamType.TEAM_2, player.getLocation());
            player.sendMessage("§aTeam 2 Spawn gesetzt.");
            return true;
        }

        player.sendMessage("§cBenutzung: /set <team1|team2>");
        return true;
    }
}
