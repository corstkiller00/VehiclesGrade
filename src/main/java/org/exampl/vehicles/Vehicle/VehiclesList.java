package org.exampl.vehicles.Vehicle;

import org.bukkit.entity.ArmorStand;

import java.util.*;

public class VehiclesList {

    private static VehiclesList vehiclesList;
    private Map<ArrayList<ArmorStand>, Vehicle> vehicles = new HashMap<>();

    private VehiclesList (){}

    public static VehiclesList getVehiclesList() {
        if(VehiclesList.vehiclesList == null){
            vehiclesList = new VehiclesList();
        }
        return vehiclesList;
    }

    public void addVehicleToList(ArrayList<ArmorStand> armorStands, Vehicle vehicle){
        vehicles.put(armorStands, vehicle);
    }

    public void removeVehicleFromList(UUID uuid){
        vehicles.remove(uuid);
    }

    public Vehicle getVehicleFromList(UUID uuid){

        for (Map.Entry<ArrayList<ArmorStand>, Vehicle> entry : vehicles.entrySet()) {
            for (ArmorStand stand : entry.getKey()) {
                if (stand.getUniqueId().equals(uuid)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

}
