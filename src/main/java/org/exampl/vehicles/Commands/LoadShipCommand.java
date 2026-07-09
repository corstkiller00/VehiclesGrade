package org.exampl.vehicles.Commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.exampl.vehicles.ShipCreator.LoadShip;
import org.exampl.vehicles.ShipCreator.PasteShip;
import org.exampl.vehicles.ShipCreator.SaveShip;
import org.exampl.vehicles.ShipCreator.ShipStructure;
import org.exampl.vehicles.Vehicles;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;

public class LoadShipCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }


        if (args.length != 1) {
            player.sendMessage("/loadship <name>");
            return true;
        }

        String shipName = args[0];

        File file = new File(Vehicles.getVehicles().getDataFolder(),
                "ships/" + shipName + ".json");

        if (!file.exists()) {
            player.sendMessage("Ship '" + shipName + "' does not exist.");
            return true;
        }

        try (Reader reader = new FileReader(file)) {

            ShipStructure ship = Vehicles.getVehicles().getGson().fromJson(reader, ShipStructure.class);

            new PasteShip().pasteShip(player.getLocation(), ship);

            player.sendMessage("Loaded ship '" + shipName + "'.");

        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage("Failed to load ship.");
        }

        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList(); // No arguments needed
    }
}
