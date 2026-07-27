package org.exampl.vehicles.Listener;

import org.bukkit.craftbukkit.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.exampl.vehicles.Vehicle.Vehicle;
import org.exampl.vehicles.Vehicle.VehiclesList;
import org.exampl.vehicles.helper.MountManager;

public class DismountArmorStandListener implements Listener {

    @EventHandler
    public void onVehicleExit(EntityDismountEvent event) {



        // Only care about ArmorStand vehicles
        if (!(event.getDismounted() instanceof CraftArmorStand)) {
            return;
        }

        // Only care about players dismounting
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if(MountManager.getMountManager().hasPlayerGotOnMount(player)) {
            // Cancel the dismount
            event.setCancelled(true);
        }else{
            Vehicle vehicle = VehiclesList.getVehiclesList().getVehicleFromList(event.getDismounted().getUniqueId());
            if(vehicle != null){
                vehicle.deleteVehicle();
            }
        }

    }
}
