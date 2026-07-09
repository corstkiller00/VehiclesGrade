package org.exampl.vehicles.Vehicle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Vector;

public class Block {

    private BlockDisplay blockDisplay;
    private final Vector offsetPosition;
    private ArmorStand armorStand;
    private Material material;

    public Block(Vector offsetPosition, Material material, ArmorStand armorStand) {
        this.offsetPosition = offsetPosition;
        this.armorStand = armorStand;
        this.material = material;
        this.blockDisplay = generateDisplayBlock();

        /*

        Vector forward = standLocation.getDirection().normalize().multiply(2);

        Location target = standLocation.add(forward);
        fallingBlock.teleport(target);

         */

    }



    private BlockDisplay generateDisplayBlock(){

        BlockDisplay display = armorStand.getWorld().spawn(
                armorStand.getLocation().clone().add(this.getOffsetPosition()),
                BlockDisplay.class
        );

        display.setBlock(Bukkit.createBlockData(this.material));

        display.setInterpolationDuration(2);
        display.setInterpolationDelay(0);
        display.setTeleportDuration(2);

        return display;
    }

    public void setBlockToOffSetPosition(){

    }

    public BlockDisplay getBlockDisplay() {
        return blockDisplay;
    }

    public Vector getOffsetPosition() {
        return offsetPosition;
    }
}
