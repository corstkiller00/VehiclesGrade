package org.exampl.vehicles.ShipCreator;

import org.bukkit.entity.ArmorStand;
import org.exampl.vehicles.Vehicles;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class SaveShip{

    public void saveShip(ArmorStand armorStand, int radius) {

        org.exampl.vehicles.ShipCreator.BlockScraper blockScraper = new org.exampl.vehicles.ShipCreator.BlockScraper();

        ShipStructure ship = blockScraper.getBlocksAround(armorStand, radius, "temp");

        File file = new File(Vehicles.getVehicles().getDataFolder(), "ships/pirate.json");

        try {
            saveShipToJSON(ship, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void saveShipToJSON(ShipStructure ship, File file) throws IOException {

        file.getParentFile().mkdirs();

        try (Writer writer = new FileWriter(file)) {
            Vehicles.getVehicles().getGson().toJson(ship, writer);
        }
    }
}
