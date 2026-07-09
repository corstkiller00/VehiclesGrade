package org.exampl.vehicles.Vehicle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.exampl.vehicles.ShipCreator.RelativeBlock;
import org.exampl.vehicles.ShipCreator.ShipStructure;
import org.exampl.vehicles.Vehicles;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Vehicle {

    private final org.exampl.vehicles.Vehicle.InvisibleVehicle invisibleVehicle;
    private ArrayList<org.exampl.vehicles.Vehicle.Block> blocks = new ArrayList<>();
    private String name;
    private final double maxSpeed = 0.1;
    private double acceleration = 0.002;
    private double currentSpeed = 0.0;
    private Player player;
    private boolean sailsDown = false;
    private boolean isAnchored = true;
    private Location cachedCentre;
    private double cachedYaw;
    private boolean fullSpeed = false;


    public boolean isSailsDown() {
        return sailsDown;
    }

    public void setSailsDown(boolean sailsDown) {
        this.sailsDown = sailsDown;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isAnchored() {
        return isAnchored;
    }

    public void setAnchored(boolean anchored) {
        isAnchored = anchored;
    }

    public void setCurrentSpeed(double currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public Vehicle(Player player) {
        this.invisibleVehicle = createInvisibleVehicle(player);

        if (this.invisibleVehicle == null)
            return;

        this.player = player;
        this.name = name;
    }

    public org.exampl.vehicles.Vehicle.InvisibleVehicle getInvisibleVehicle() {
        return invisibleVehicle;
    }

    private org.exampl.vehicles.Vehicle.InvisibleVehicle createInvisibleVehicle(Player player) {

        try {
            return new org.exampl.vehicles.Vehicle.InvisibleVehicle(player, getShipStructure());
        }
        catch (IllegalStateException ex) {
            player.sendMessage("§cLook at the surface of the water to spawn your ship.");
            return null;
        }
    }

    public void changeHeadPoseRotation(double degrees){
        ArmorStand stand = this.invisibleVehicle.getStand();

        EulerAngle pose = stand.getHeadPose();
        pose = pose.setY(pose.getY() + Math.toRadians(degrees)); // if degrees is actually in degrees

        stand.setHeadPose(pose);
    }

    public void createVehicle() {
        org.exampl.vehicles.Vehicle.VehiclesList.getVehiclesList().addVehicleToList(this.invisibleVehicle.getStand().getUniqueId(), this);
        org.exampl.vehicles.Vehicle.Block block = new org.exampl.vehicles.Vehicle.Block(new Vector(1, 0, 0), Material.OAK_WOOD, this.invisibleVehicle.getStand());
        this.blocks.add(block);
        org.exampl.vehicles.Vehicle.Block block2 = new org.exampl.vehicles.Vehicle.Block(new Vector(2, 0, 0), Material.OAK_WOOD, this.invisibleVehicle.getStand());
        this.blocks.add(block2);
        player.getInventory().setHeldItemSlot(0);
    }

    private void createBlocks(List<RelativeBlock> blocks){

        org.exampl.vehicles.Vehicle.VehiclesList.getVehiclesList().addVehicleToList(this.invisibleVehicle.getStand().getUniqueId(), this);

        for(RelativeBlock block : blocks){

            BlockData blockData = Bukkit.createBlockData(block.getBlockData());

            if(blockData.getMaterial().equals(Material.AIR)){
                continue;
            }

            org.exampl.vehicles.Vehicle.Block blockDisplay = new org.exampl.vehicles.Vehicle.Block(new Vector(block.getX(), block.getY(),block.getZ()), blockData.getMaterial(),  this.invisibleVehicle.getStand());

            this.blocks.add(blockDisplay);
        }

        player.getInventory().setHeldItemSlot(0);
    }

    public void createVehicleFromSave(){

        String shipName = "pirate";

        File file = new File(Vehicles.getVehicles().getDataFolder(),
                "ships/" + shipName + ".json");

        if (!file.exists()) {
            player.sendMessage("Ship '" + shipName + "' does not exist.");
        }

        try (Reader reader = new FileReader(file)) {

            ShipStructure ship = Vehicles.getVehicles().getGson().fromJson(reader, ShipStructure.class);

            createBlocks(ship.getBlocks());

        }catch (IOException e) {
            e.printStackTrace();
            player.sendMessage("Failed to load ship.");
        }
    }


    private ShipStructure getShipStructure(){

        ShipStructure ship = null;

        String shipName = "pirate";

        File file = new File(Vehicles.getVehicles().getDataFolder(),
                "ships/" + shipName + ".json");

        if (!file.exists()) {
            player.sendMessage("Ship '" + shipName + "' does not exist.");
        }

        try (Reader reader = new FileReader(file)) {

            ship = Vehicles.getVehicles().getGson().fromJson(reader, ShipStructure.class);


        }catch (IOException e) {
            e.printStackTrace();
            player.sendMessage("Failed to load ship.");
        }

        return  ship;
    }



    private void deleteBlocksInVehicle(){
        for (Block block : blocks) {
            block.getBlockDisplay().remove();
        }
    }


    private void renderBlocks() {

        for (Block block : blocks) {

            Vector offset = block.getOffsetPosition();

            Vector forward = new Vector(
                    -Math.sin(cachedYaw),
                    0,
                    Math.cos(cachedYaw)
            ).normalize();

            Vector right = forward.clone()
                    .crossProduct(new Vector(0, 1, 0))
                    .normalize();

            /*
            Vector worldOffset =
                    right.multiply(offset.getX())
                            .add(forward.multiply(offset.getZ()));

             */

            Vector worldOffset = right.multiply(offset.getX())
                    .add(forward.multiply(offset.getZ()))
                    .setY(offset.getY());

            Location target = cachedCentre.clone().add(worldOffset);

            BlockDisplay display = block.getBlockDisplay();

            display.teleport(target);

            Location base = invisibleVehicle.getStand().getLocation().clone()
                    .add(0, invisibleVehicle.getStand().getHeight() / 2.0, 0);

            // Body yaw (world)
            float bodyYaw = base.getYaw();

// Head pose yaw (local, radians)
            double headYawRad = invisibleVehicle.getStand().getHeadPose().getY();

// Combine
            double finalYawRad = Math.toRadians(bodyYaw) + headYawRad;

            float yawDegT = (float) Math.toDegrees(finalYawRad);
            block.getBlockDisplay().setRotation(yawDegT, 0f);
        }


    }

    public void startMovementLoop() {

        new BukkitRunnable() {
            @Override
            public void run() {

                if (invisibleVehicle.getStand().isDead()
                        || !invisibleVehicle.getStand().getPassengers().contains(player)) {

                    cancel();
                    invisibleVehicle.getStand().remove();
                    deleteBlocksInVehicle();

                    VehiclesList.getVehiclesList()
                            .removeVehicleFromList(invisibleVehicle.getStand().getUniqueId());

                    HotbarSnapshotDatabase.getHotbarSnapshotDatabase()
                            .restorePlayerHotbarSnapshot(player);

                    HotbarSnapshotDatabase.getHotbarSnapshotDatabase()
                            .removePlayerSnapshotFromDatabase(player);
                    return;
                }

                // -------------------------
                // SPEED LOGIC (unchanged)
                // -------------------------
                if (sailsDown && !isAnchored && currentSpeed < maxSpeed) {
                    currentSpeed = Math.min(maxSpeed, currentSpeed + acceleration);
                }

                if(currentSpeed == maxSpeed && !fullSpeed){
                    fullSpeed = true;
                    player.playSound(
                            player.getLocation(),
                            Sound.ENTITY_ENDER_DRAGON_FLAP,
                            0.08f,
                            1.8f
                    );
                }

                if(fullSpeed && currentSpeed < maxSpeed){
                    fullSpeed = false;
                }

                if (isAnchored) {
                    currentSpeed = 0.0;
                }

                if (!sailsDown && currentSpeed > 0.0) {
                    currentSpeed = Math.max(0.0, currentSpeed - acceleration);
                }

                // -------------------------
                // DETERMINE YAW (stable)
                // -------------------------
                double bodyYaw = Math.toRadians(invisibleVehicle.getStand().getLocation().getYaw());
                double headYaw = invisibleVehicle.getStand().getHeadPose().getY();

                cachedYaw = bodyYaw + headYaw;

                Vector velocity = new Vector(
                        -Math.sin(cachedYaw),
                        0,
                        Math.cos(cachedYaw)
                ).multiply(currentSpeed);

                //Below is for 1.21.11


                Location loc = invisibleVehicle.getStand().getLocation().clone();
                loc.add(velocity);

               // invisibleVehicle.getStand().teleport(loc);



                CraftArmorStand craftStand = (CraftArmorStand) invisibleVehicle.getStand();
                net.minecraft.world.entity.decoration.ArmorStand nms = craftStand.getHandle();

                float yaw = invisibleVehicle.getStand().getLocation().getYaw();
                float pitch = invisibleVehicle.getStand().getLocation().getPitch();

                nms.absMoveTo(
                        loc.getX(),
                        loc.getY(),
                        loc.getZ(),
                        yaw,
                        pitch
                );

                nms.setYHeadRot(yaw);



                // -------------------------
                // CACHE CENTRE (IMPORTANT)
                // -------------------------
                cachedCentre = invisibleVehicle.getStand().getLocation().clone()
                        .add(0, invisibleVehicle.getStand().getHeight() / 2.0, 0);

                renderBlocks();

            }
        }.runTaskTimer(Vehicles.getVehicles(), 1L, 1L);
    }


}
