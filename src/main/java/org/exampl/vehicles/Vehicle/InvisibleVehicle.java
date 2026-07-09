package org.exampl.vehicles.Vehicle;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;
import org.exampl.vehicles.ShipCreator.RelativeBlock;
import org.exampl.vehicles.ShipCreator.ShipPartType;
import org.exampl.vehicles.ShipCreator.ShipStructure;
import org.exampl.vehicles.Vehicles;

public class InvisibleVehicle {

    private final ArmorStand stand;
    private final Player player;

    public InvisibleVehicle(Player player, ShipStructure ship) {
        this.player = player;

        int lowestYBlock = getLowestYBlock(ship);

        Location spawnLocation = findSpawnLocation(player, lowestYBlock);

        if (spawnLocation == null) {
            throw new IllegalStateException("No suitable water found.");
        }


        this.stand = player.getWorld().spawn(spawnLocation, ArmorStand.class, as -> {
            as.setInvisible(false);
            as.setGravity(false);   //Needs to be true
            as.setSmall(true);
            as.setInvulnerable(true);
        });

        stand.addPassenger(player);
        setPersistentDataContainer();

        org.exampl.vehicles.Vehicle.HotbarSnapshot snapshot = new org.exampl.vehicles.Vehicle.HotbarSnapshot(player, true);
        org.exampl.vehicles.Vehicle.HotbarSnapshotDatabase.getHotbarSnapshotDatabase().addPlayerSnapshotToDatabase(player, snapshot);
        snapshot.setShipHotbar();

    }

    private void setPersistentDataContainer() {
        NamespacedKey key = new NamespacedKey(Vehicles.getVehicles(), "ship_part");

        stand.getPersistentDataContainer().set(
                key,
                PersistentDataType.STRING,
                ShipPartType.WHEEL.name()
        );
    }



    private Location findSpawnLocation(Player player, int lowestYBlock) {

        RayTraceResult trace = player.rayTraceBlocks(
                40,
                FluidCollisionMode.ALWAYS
        );

        if (trace == null || trace.getHitBlock() == null)
            return null;

        org.bukkit.block.Block hit = trace.getHitBlock();

        World world = hit.getWorld();

        int x = hit.getX();
        int z = hit.getZ();

        // Find the surface of this water column
        for (int y = world.getMaxHeight() - 1; y >= world.getMinHeight(); y--) {

            org.bukkit.block.Block current = world.getBlockAt(x, y, z);

            if (current.getType() != Material.WATER)
                continue;

            Block above = world.getBlockAt(x, y + (1 + lowestYBlock), z);

            if (!above.isPassable())
                continue;

            return new Location(
                    world,
                    x + 0.5,
                    y + (0.5 + lowestYBlock),
                    z + 0.5,
                    player.getLocation().getYaw(),
                    0
            );
        }

        return null;
    }


    private int getLowestYBlock(ShipStructure shipStructure){

        int lowestYBlock = 0;

        for(RelativeBlock relativeBlock : shipStructure.getBlocks()){
            if(relativeBlock.getY() < lowestYBlock){
                lowestYBlock = relativeBlock.getY();
            }
        }

        System.out.println(lowestYBlock);
        return Math.abs(lowestYBlock);
    }

    public ArmorStand getStand() {
        return stand;
    }

    public Player getPlayer() {
        return player;
    }
}
