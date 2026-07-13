package org.exampl.vehicles.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.exampl.vehicles.Cannons.Cannon;
import org.exampl.vehicles.Cannons.CannonBall;
import org.exampl.vehicles.Vehicle.Vehicle;

import java.util.Collections;
import java.util.List;

public class ShootCannonBall implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (!player.hasPermission("spawnboat.use")) {
            player.sendMessage("You do not have permission to use this command.");
            return true;
        }

        new Cannon(player.getLocation(), player);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList(); // No arguments needed
    }
}