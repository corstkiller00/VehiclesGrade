package org.exampl.vehicles.ShipCreator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.exampl.vehicles.Vehicles;

import java.util.ArrayList;
import java.util.List;

public class BlockScraper {

    public ShipStructure getBlocksAround(ArmorStand stand, int radius, String shipName) {
        List<RelativeBlock> blocks = new ArrayList<>();

        Location center = stand.getLocation();
        World world = center.getWorld();

        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();

        // Use the same yaw that the renderer uses
        double shipYaw = Math.toRadians(center.getYaw()) + stand.getHeadPose().getY();

        Vector forward = new Vector(
                -Math.sin(shipYaw),
                0,
                Math.cos(shipYaw)
        ).normalize();

        Vector right = forward.clone()
                .crossProduct(new Vector(0, 1, 0))
                .normalize();

        // ------------------------
        // Save Blocks
        // ------------------------

        for (int x = cx - radius; x <= cx + radius; x++) {
            for (int y = cy - radius; y <= cy + radius; y++) {
                for (int z = cz - radius; z <= cz + radius; z++) {

                    Block block = world.getBlockAt(x, y, z);

                    if(block.getBlockData().getMaterial().equals(Material.AIR)){
                        continue;
                    }

                    Vector worldOffset = new Vector(
                            x - cx,
                            y - cy,
                            z - cz
                    );

                    double localX = worldOffset.dot(right);
                    double localZ = worldOffset.dot(forward);

                    blocks.add(new RelativeBlock(
                            (int) Math.round(localX),
                            y - cy,
                            (int) Math.round(localZ),
                            block.getBlockData().getAsString()
                    ));
                }
            }
        }

        //Save ArmorStands

        List<ArmorStandData> armorStands = new ArrayList<>();

        NamespacedKey key = new NamespacedKey(Vehicles.getVehicles(), "ship_part");

        for (Entity entity : world.getNearbyEntities(center, radius, radius, radius)) {

            if (!(entity instanceof ArmorStand armorStand))
                continue;

            if (armorStand.equals(stand))
                continue;

            if (armorStand.getLocation().distanceSquared(center) > radius * radius)
                continue;

            Location loc = armorStand.getLocation();

            Vector worldOffset = loc.toVector().subtract(center.toVector());

            double localX = worldOffset.dot(right);
            double localZ = worldOffset.dot(forward);

            // Store yaw relative to the ship
            double armorYaw = Math.toRadians(loc.getYaw());
            float relativeYaw = (float) Math.toDegrees(armorYaw - shipYaw);

            ArmorStandData armorStandData = new ArmorStandData(
                    localX,
                    worldOffset.getY(),
                    localZ,
                    relativeYaw,
                    loc.getPitch(),
                    armorStand.getCustomName(),
                    armorStand.isInvisible(),
                    armorStand.isMarker(),
                    armorStand.isSmall(),
                    armorStand.hasArms(),
                    !armorStand.hasBasePlate()
            );

            String partType = armorStand.getPersistentDataContainer().get(
                    key,
                    PersistentDataType.STRING
            );

            if (partType != null) {
                armorStandData.setPartType(partType);
            }

            armorStands.add(armorStandData);

        }
        return new ShipStructure(shipName, blocks, armorStands);
    }
}


