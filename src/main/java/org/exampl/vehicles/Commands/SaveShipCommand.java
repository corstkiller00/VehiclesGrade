package org.exampl.vehicles.Commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.exampl.vehicles.ShipCreator.BlockScraper;
import org.exampl.vehicles.ShipCreator.SaveShip;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class SaveShipCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        ArmorStand armorStand = getClosestArmorStand(player, 30);
        new SaveShip().saveShip(armorStand, 30);
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList(); // No arguments needed
    }

    public ArmorStand getClosestArmorStand(Player player, double radius) {

        Location location = player.getLocation();

        ArmorStand closest = null;
        double closestDistanceSquared = radius * radius;

        for (Entity entity : player.getWorld().getNearbyEntities(location, radius, radius, radius)) {

            if (!(entity instanceof ArmorStand armorStand))
                continue;

            double distanceSquared = armorStand.getLocation().distanceSquared(location);

            if (distanceSquared < closestDistanceSquared) {
                closest = armorStand;
                closestDistanceSquared = distanceSquared;
            }
        }

        return closest;
    }
}
