package org.exampl.vehicles.Listener;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.A;
import org.exampl.vehicles.Cannons.Cannon;
import org.exampl.vehicles.Cannons.CannonManager;
import org.exampl.vehicles.ShipCreator.ShipPartType;
import org.exampl.vehicles.Vehicle.Vehicle;
import org.exampl.vehicles.Vehicles;
import org.exampl.vehicles.helper.MountManager;

import java.util.UUID;

public class SneakListener implements Listener {

    private final NamespacedKey partKey;

    public SneakListener() {
        this.partKey = new NamespacedKey(Vehicles.getVehicles(), "ship_part");
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {

        if (!event.isSneaking())
            return; // Only when they start sneaking

        Player player = event.getPlayer();

        if(player.isInsideVehicle()){
            return; //return if player is already on a vehicle
        }

        for (Entity entity : player.getNearbyEntities(1, 2, 1)) {

            if (!(entity instanceof ArmorStand stand))
                continue;

            PersistentDataContainer pdc = stand.getPersistentDataContainer();

            if (!pdc.has(partKey, PersistentDataType.STRING))
                continue;

            String part = pdc.get(partKey, PersistentDataType.STRING);

            if(part == null){
                continue;
            }

            if (part.equals(ShipPartType.WHEEL.name())) {
                //Vehicle vehicle = new Vehicle(player);
                //vehicle.createVehicle();
               // vehicle.startMovementLoop();
            }

            if (part.equals(ShipPartType.CANNON.name())) {
                cannonInteract(stand, player);
            }
        }
    }


    private void cannonInteract(ArmorStand stand, Player player){
        org.exampl.vehicles.Cannons.Cannon cannon = CannonManager.getCannonManager().getCannonFromManager(stand.getUniqueId());

        if(cannon != null){
            MountManager.getMountManager().addPlayerToManager(player);
            MountManager.getMountManager().removePlayerFromManagerInOneTick(player);
            cannon.playerUsingStand(player);
        }
    }
}
