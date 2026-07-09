package org.exampl.vehicles.Vehicle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VehiclesList {

    private static VehiclesList vehiclesList;
    private Map<UUID, Vehicle> vehicles = new HashMap<>();

    private VehiclesList (){}

    public static VehiclesList getVehiclesList() {
        if(VehiclesList.vehiclesList == null){
            vehiclesList = new VehiclesList();
        }
        return vehiclesList;
    }

    public void addVehicleToList(UUID uuid, Vehicle vehicle){
        vehicles.put(uuid, vehicle);
    }

    public void removeVehicleFromList(UUID uuid){
        vehicles.remove(uuid);
    }

    public Vehicle getVehicleFromList(UUID uuid){
        return vehicles.get(uuid);
    }

}
