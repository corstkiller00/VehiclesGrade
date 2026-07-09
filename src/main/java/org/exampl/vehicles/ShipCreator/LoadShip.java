package org.exampl.vehicles.ShipCreator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.exampl.vehicles.Vehicles;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class LoadShip {
    /*

    public ShipStructure loadShip(File file) throws IOException {

        try (Reader reader = new FileReader(file)) {
            return Vehicles.getVehicles().getGson().fromJson(reader, ShipStructure.class);
        }

        ShipStructure ship = loadShip(
                new File(Vehicles.getVehicles().getDataFolder(), "ships/pirate.json")
        );

        PasteShip pasteShip = new PasteShip();
        pasteShip.pasteShip(location, ship.getBlocks());
    }

     */

}
