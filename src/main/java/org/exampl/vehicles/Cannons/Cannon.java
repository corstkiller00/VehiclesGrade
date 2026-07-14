package org.exampl.vehicles.Cannons;

import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.exampl.vehicles.Vehicles;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Cannon {

    private ArmorStand armorStands;
    private BlockDisplay blastFurnance;
    private ItemDisplay barrel;
    private Interaction pivot;
    private Quaternionf initialRotation;
    private ArmorStand stand;

    public Cannon(Location cannonLocation, Player player){

        //Get the direction to face the furnace

        BlockFace face = getClosestFacing(player.getLocation().getYaw());

        //Define the forward vector from the face of the furnace

        Vector forward = new Vector(
                face.getModX(),
                0,
                face.getModZ()
        );


        Location furnaceLoc = cannonLocation.clone()
                        .add(forward.clone().multiply(1))
                        .add(0, 0, 0);     //Raise the height 0.45 from armor stand base


        furnaceLoc.setYaw(0);
        furnaceLoc.setPitch(0);

        Location barrelLoc = furnaceLoc.clone()
                .add(forward.clone().multiply(1.20))
                .add(0, 0.5, 0);    //Can maybe increase to stop transform Y change later

        World world = cannonLocation.getWorld();

        // Match player yaw but keep the cannon level
        float yaw = player.getLocation().getYaw();


        Location baseLoc = cannonLocation.clone();
        baseLoc.setYaw(yaw);
        baseLoc.setPitch(0);

        // Create the parent Armor stand
        stand = world.spawn(baseLoc, ArmorStand.class, as -> {
            as.setInvisible(false);
            as.setMarker(true);
            as.setGravity(false);
            as.setInvulnerable(true);
            as.setRotation(yaw, 0);
        });

        // Base (Blast Furnace)
        blastFurnance = world.spawn(furnaceLoc, BlockDisplay.class, bd -> {
            Directional furnace = (Directional) Bukkit.createBlockData(Material.BLAST_FURNACE);

            furnace.setFacing(
                    getClosestFacing(player.getLocation().getYaw())
            );

            bd.setBlock(furnace);

            Transformation t = new Transformation(
                    new Vector3f(-0.5f, 0f, -0.5f),  //offset of the 0,0 corner.
                    new Quaternionf(),
                    new Vector3f(1f, 1f, 1f),
                    new Quaternionf()
            );

            bd.setTransformation(t);
        });

        // Barrel (Lightning Rod)
        barrel = world.spawn(barrelLoc, ItemDisplay.class, id -> {

            id.setItemStack(new ItemStack(Material.LIGHTNING_ROD));

            Transformation t = new Transformation(

                    new Vector3f(0f, 0f, 0f),

                    // Rotate onto its side
                    new Quaternionf()
                            // Turn cannon to player direction
                            .rotateY((float)Math.toRadians(-getYaw(face)))

                            // Lay the lightning rod like a barrel
                            .rotateX((float)Math.toRadians(90)),

                    // Make it longer
                    new Vector3f(1f, 2.2f, 1f),

                    new Quaternionf()
            );

            id.setTransformation(t);
        });

        rotateCannon(player);
    }

    public void shoot() {

        Location pivotLoc = pivot.getLocation();

        // Direction the cannon is pointing
        Vector direction = pivotLoc.getDirection().normalize();

        // Spawn the cannonball slightly in front of the barrel
        Location muzzle = pivotLoc.clone()
                .add(direction.multiply(2.2))
                .add(0, 0.35, 0);

        new CannonBall(muzzle, direction);
    }

    private void effects(Location spawn, Player player){

        World world = player.getWorld();

        world.spawnParticle(
                Particle.SMOKE,
                spawn,
                30,
                0.2, 0.2, 0.2,
                0.02
        );

        world.spawnParticle(
                Particle.EXPLOSION,
                spawn,
                1
        );

        world.playSound(
                spawn,
                Sound.ENTITY_GENERIC_EXPLODE,
                2.0f,
                0.7f
        );
    }



    private void rotateCannon(Player player) {

        new BukkitRunnable() {
            @Override
            public void run() {

                //Set max yaw (left to right)


                // baseYaw = the yaw the cannon's base/stand is fixed at (e.g. the yaw captured when it was placed)
                float baseYaw = stand.getLocation().getYaw();


                float playerYaw = player.getLocation().getYaw();

// Relative yaw = how far the player has turned away from the cannon's base facing
                float relativeYaw = playerYaw - baseYaw;

// Normalize to -180..180 so clamping behaves correctly across the wrap-around
                relativeYaw = ((relativeYaw + 180f) % 360f + 360f) % 360f - 180f;

// Clamp relative to the cannon, not the world
                float clampedRelativeYaw = Math.max(-20, Math.min(20, relativeYaw));

                // Convert back to world yaw for the actual transform/rotation
                float finalYaw = baseYaw + clampedRelativeYaw;


                // Limit cannon elevation
                float pitch = player.getLocation().getPitch();
                pitch = Math.max(-20, Math.min(20, pitch));

                Quaternionf rotation = new Quaternionf()
                        // Turn cannon to player direction
                        .rotateY((float)Math.toRadians(-finalYaw))

                        // Lay the lightning rod like a barrel
                        .rotateX((float)Math.toRadians(90))

                        // Aim slightly up/down
                        .rotateX((float)Math.toRadians(pitch));


                barrel.setTransformation(new Transformation(
                        new Vector3f(0f, 0f, 0f),
                        rotation,
                        new Vector3f(1f, 2.2f, 1f),
                        new Quaternionf()
                ));

            }

        }.runTaskTimer(Vehicles.getVehicles(), 1L, 1L);
    }


    private BlockFace getClosestFacing(float yaw) {

        yaw = yaw % 360;

        if (yaw < 0) {
            yaw += 360;
        }

        if (yaw >= 45 && yaw < 135) {
            return BlockFace.WEST;
        }

        if (yaw >= 135 && yaw < 225) {
            return BlockFace.NORTH;
        }

        if (yaw >= 225 && yaw < 315) {
            return BlockFace.EAST;
        }

        return BlockFace.SOUTH;
    }


    public static float getYaw(BlockFace face) {
        return switch (face) {
            case SOUTH -> 0f;
            case WEST  -> 90f;
            case NORTH -> 180f;
            case EAST  -> -90f; // or 270f
            default    -> 0f;
        };
    }
}
