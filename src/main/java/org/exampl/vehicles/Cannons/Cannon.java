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
    private BlockDisplay breech;
    private ItemDisplay barrel;
    private Interaction pivot;
    private Quaternionf initialRotation;

    public Cannon(Location cannonLocation, Player player){


        Vector forward = cannonLocation.getDirection().setY(0).normalize();

        Location furnaceLoc = cannonLocation.clone();

        furnaceLoc.setYaw(0);
        furnaceLoc.setPitch(0);

        Location barrelLoc = furnaceLoc.clone()
                .add(forward.clone().multiply(0.45))
                .add(0, 0.45, 0);

        World world = cannonLocation.getWorld();

        // Match player yaw but keep the cannon level
        float yaw = player.getLocation().getYaw();

        //get Y height
       // float pitch = player.getLocation().getPitch();

        Location baseLoc = cannonLocation.clone();
        baseLoc.setYaw(yaw);
        baseLoc.setPitch(0);

        // Parent
        ArmorStand stand = world.spawn(baseLoc, ArmorStand.class, as -> {
            as.setInvisible(false);
            as.setMarker(true);
            as.setGravity(false);
            as.setInvulnerable(true);
            as.setRotation(yaw, 0);
        });

        // Base (Blast Furnace)
        breech = world.spawn(furnaceLoc, BlockDisplay.class, bd -> {
            bd.setBlock(Bukkit.createBlockData(Material.BLAST_FURNACE));

            Transformation t = new Transformation(
                    new Vector3f(0f, 0f, 0f),
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

                    // Offset from blast furnace
                    new Vector3f(0f, 0.45f, -0.70f),

                    // Rotate onto its side
                    new Quaternionf()
                            .rotateZ((float)Math.toRadians(90)),

                    // Make it longer
                    new Vector3f(1f, 2.2f, 1f),

                    new Quaternionf()
            );

            id.setTransformation(t);
        });


        Quaternionf rotation = new Quaternionf()
                .rotateY((float)Math.toRadians(-yaw))
                .rotateZ((float)Math.toRadians(90));

        Transformation t = new Transformation(
                new Vector3f(0, 0, 0),
                rotation,
                new Vector3f(1f, 2.2f, 1f),
                new Quaternionf()
        );


        barrel.setTransformation(t);

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

    private void spawnBlastFurnace(Location base){

        breech = (BlockDisplay) base.getWorld().spawnEntity(
                base,
                EntityType.BLOCK_DISPLAY
        );

        breech.setBlock(Material.BLAST_FURNACE.createBlockData());
    }


    private void spawnLightningRoad(Location base){
/*
       barrel = (BlockDisplay) base.getWorld().spawnEntity(
                base,
                EntityType.BLOCK_DISPLAY
        );

        barrel.setBlock(Material.LIGHTNING_ROD.createBlockData());

 */
    }

    private void rotateCannon(Player player) {

        new BukkitRunnable() {
            @Override
            public void run() {

                float yaw = player.getLocation().getYaw();

                // Limit cannon elevation
                float pitch = player.getLocation().getPitch();
                pitch = Math.max(-30, Math.min(30, pitch));

                Quaternionf rotation = new Quaternionf()
                        // Turn cannon to player direction
                        .rotateY((float)Math.toRadians(-yaw))

                        // Lay the lightning rod like a barrel
                        .rotateX((float)Math.toRadians(90))

                        // Aim slightly up/down
                        .rotateX((float)Math.toRadians(pitch));


                barrel.setTransformation(new Transformation(
                        new Vector3f(0f, 0.45f, -0.70f),
                        rotation,
                        new Vector3f(1f, 2.2f, 1f),
                        new Quaternionf()
                ));

            }

        }.runTaskTimer(Vehicles.getVehicles(), 1L, 1L);
    }

    public static float getYawFromVector(Vector v) {
        return (float) Math.toDegrees(Math.atan2(-v.getX(), v.getZ()));
    }

}
