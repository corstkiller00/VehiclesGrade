package org.exampl.vehicles.Commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.exampl.vehicles.Vehicle.InvisibleVehicle;
import org.exampl.vehicles.Vehicle.Vehicle;

import java.util.Collections;
import java.util.List;

public class SpawnBoatCommand implements CommandExecutor, TabCompleter {

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


        Vehicle vehicle = new Vehicle(player);
        //vehicle.createVehicle();

        if(vehicle.getInvisibleVehicle() == null){
            return true;
        }
        vehicle.createVehicleFromSave();
        vehicle.startMovementLoop();
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList(); // No arguments needed
    }
}