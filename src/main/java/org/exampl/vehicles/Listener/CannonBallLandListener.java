package org.exampl.vehicles.Listener;

import org.bukkit.*;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class CannonBallLandListener implements Listener {

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof FallingBlock fallingBlock &&
                fallingBlock.getBlockData().getMaterial() == Material.IRON_BLOCK) {

            event.setCancelled(true);
            World world = event.getEntity().getWorld();
            Location location = event.getEntity().getLocation();
            world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 0.8f, 0.6f);
            world.spawnParticle(Particle.EXPLOSION, location, 1);
            world.spawnParticle(Particle.SMOKE, location, 20, 0.3, 0.3, 0.3, 0.02);
            fallingBlock.remove();

        }
    }
}
