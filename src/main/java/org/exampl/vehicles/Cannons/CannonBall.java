package org.exampl.vehicles.Cannons;

import org.bukkit.*;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.exampl.vehicles.Vehicles;

public class CannonBall {

    private BlockDisplay cannonball;
    private Location location;
    private Vector direction;

    public CannonBall(Location location, Vector direction) {
        this.location = location;
        this.direction = direction;
        createCannonBall();

    }

    private void createCannonBall(){

        World world =  location.getWorld();

         cannonball = (BlockDisplay) world.spawnEntity(
                location,
                EntityType.BLOCK_DISPLAY
        );

        cannonball.setBlock(Material.IRON_BLOCK.createBlockData());

        Vector velocity = direction.multiply(2.8);

        moveCannonBall(location,velocity);
    }

    private void moveCannonBall(Location location, Vector velocity){

       World world = location.getWorld();

        new BukkitRunnable() {

            Location position = location.clone();
            Vector vel = velocity.clone();

            @Override
            public void run() {

                Location previous = position.clone();

                vel.setY(vel.getY() - 0.04);   // gravity
                position.add(vel);

                RayTraceResult hit = world.rayTraceBlocks(
                        previous,
                        vel.clone().normalize(),
                        vel.length()
                );

                if (hit != null) {

                    world.spawnParticle(
                            Particle.EXPLOSION,
                            hit.getHitPosition().toLocation(location.getWorld()),
                            1
                    );

                    world.playSound(
                            hit.getHitPosition().toLocation(world),
                            Sound.ENTITY_GENERIC_EXPLODE,
                            1f,
                            0.6f
                    );

                    cannonball.remove();
                    cancel();
                    return;
                }

                cannonball.teleport(position);
            }

        }.runTaskTimer(Vehicles.getVehicles(), 1L, 1L);
    }
}
