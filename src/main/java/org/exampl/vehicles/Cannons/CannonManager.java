package org.exampl.vehicles.Cannons;

import org.exampl.vehicles.Vehicle.Vehicle;
import org.exampl.vehicles.Vehicle.VehiclesList;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CannonManager {


    private static CannonManager cannonManager;
    private Map<UUID, Cannon> cannons = new HashMap<>();

    private CannonManager (){}

    public static CannonManager getCannonManager() {
        if(CannonManager.cannonManager == null){
           cannonManager = new CannonManager();
        }
        return cannonManager;
    }

    public void addCannonToManager(UUID uuid, Cannon cannon){
        cannons.put(uuid, cannon);
    }

    public void removeCannonFromManager(UUID uuid){
        cannons.remove(uuid);
    }

    public Cannon getCannonFromManager(UUID uuid){
        return cannons.get(uuid);
    }
}
