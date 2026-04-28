package de.mixaghg.networkbedfight.command;

import de.mixaghg.networkbedfight.NetworkBedFightPlugin;
import org.bukkit.command.*;

public class BedFightResetCommand implements CommandExecutor {

    private final NetworkBedFightPlugin plugin;

    public BedFightResetCommand(NetworkBedFightPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("§cKeine Rechte.");
            return true;
        }
        plugin.getGameManager().resetMatch();
        sender.sendMessage("§aBedFight resetet.");
        return true;
    }
}
