package org.exampl.vehicles.Vehicle;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.exampl.vehicles.Vehicles;

public class InteractionTask extends BukkitRunnable {

    private final NamespacedKey partKey;

    public InteractionTask() {
        this.partKey = new NamespacedKey(Vehicles.getVehicles(), "ship_part");
    }

    @Override
    public void run() {

        for (Player player : Bukkit.getOnlinePlayers()) {

            ArmorStand nearbyPart = null;

            for (Entity entity : player.getNearbyEntities(1, 2, 1)) {

                if (!(entity instanceof ArmorStand stand))
                    continue;

               String print = stand.getPersistentDataContainer().get(
                        partKey,
                        PersistentDataType.STRING
                );



                PersistentDataContainer pdc = stand.getPersistentDataContainer();

                if (!pdc.has(partKey, PersistentDataType.STRING))
                    continue;

                nearbyPart = stand;
                break;
            }

            if (nearbyPart != null) {
                String part = nearbyPart.getPersistentDataContainer().get(
                        partKey,
                        PersistentDataType.STRING
                );

                player.sendActionBar("Press shift to interact with " + part);
            }
        }
    }
}