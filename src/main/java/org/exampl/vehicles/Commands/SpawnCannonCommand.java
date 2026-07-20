package org.exampl.vehicles.Commands;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.exampl.vehicles.Cannons.Cannon;
import org.exampl.vehicles.ShipCreator.ShipPartType;
import org.exampl.vehicles.Vehicles;

import java.util.Collections;
import java.util.List;

public class SpawnCannonCommand implements CommandExecutor, TabCompleter {

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



        ArmorStand stand = player.getWorld().spawn(player.getLocation(), ArmorStand.class, as -> {
            as.setInvisible(false);
            as.setGravity(false);
            as.setInvulnerable(true);
           // as.setSmall(true);
            as.setRotation(player.getYaw(), 0);
        });

        setPersistentDataContainer(stand);

        //new Cannon(stand);
        //new Cannon(player.getLocation(), player);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList(); // No arguments needed
    }

    private void setPersistentDataContainer(ArmorStand stand) {
        NamespacedKey key = new NamespacedKey(Vehicles.getVehicles(), "ship_part");

        stand.getPersistentDataContainer().set(
                key,
                PersistentDataType.STRING,
                ShipPartType.CANNON.name()
        );
    }
}