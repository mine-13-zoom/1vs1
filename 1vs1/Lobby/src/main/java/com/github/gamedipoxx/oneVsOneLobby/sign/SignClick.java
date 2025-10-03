package com.github.gamedipoxx.oneVsOneLobby.sign;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.gamedipoxx.oneVsOneLobby.LobbyMessages;
import com.github.gamedipoxx.oneVsOneLobby.LobbySQLManager;

public class SignClick implements CommandExecutor {

    // This is the command that players will run via the NPC
    // For example, /1vs1
    private static final String COMMAND_NAME = "openduelgui";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        // Optional: update the "sign lines" just like before
        // Could be used for GUI placeholders if needed
        // LobbyMessages.JOINSIGN_LINE1.setString("New Line 1");

        // Open the same GUI
        JoinGUI.openForPlayer(player);

        // Fetch database info
        LobbySQLManager.fetchFromDatabase();

        return true;
    }
}
