package org.exampl.vehicles.Vehicle;

import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.exampl.vehicles.Cannons.Cannon;
import org.exampl.vehicles.ShipCreator.ArmorStandData;
import org.exampl.vehicles.ShipCreator.RelativeBlock;
import org.exampl.vehicles.ShipCreator.ShipStructure;
import org.exampl.vehicles.Vehicle.Seats.CannonSeat;
import org.exampl.vehicles.Vehicle.Seats.Seat;
import org.exampl.vehicles.Vehicle.Seats.WheelSeat;
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
    private final double maxSpeed = 0.1;   //was 0.1
    private double acceleration = 0.002;
    private double currentSpeed = 0.0;
    private Player player;
    private boolean sailsDown = false;
    private boolean isAnchored = true;
    private Location cachedCentre;
    private double cachedYaw;
    private boolean fullSpeed = false;
    private BukkitTask vehicleTask;
    private ArrayList<ArmorStand> armorStands = new ArrayList<>();
    private ArrayList<Cannon> cannons = new ArrayList<>();
    private ArrayList<Seat> seats = new ArrayList<>();

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

        if (this.invisibleVehicle == null) {
            return;
        }

        this.player = player;
        this.name = name;

        this.seats.add(new WheelSeat(this.getInvisibleVehicle().getStand()));
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

    private Seat getNextSeat(Player player){

        Seat currentSeat = getPlayersCurrentSeat(player);

        if(currentSeat != null){

            int nextSeatIndex;

            int seatIndex = this.seats.indexOf(currentSeat);

            if(seatIndex == (this.seats.size() - 1)){
                nextSeatIndex = 0;
            }else{
                nextSeatIndex = seatIndex + 1;
            }

            return this.seats.get(nextSeatIndex);
        }

        return null;

    }

    private Seat getPlayersCurrentSeat(Player player){

        for(Seat seat : this.seats){

            if(seat.getPlayerOnSeat() == null){
                continue;
            }

            if(seat.getPlayerOnSeat().equals(player)){
                return seat;
            }
        }

        return null;
    }

    public void movePlayerToNextSeat(Player player){

        Seat nextSeat = getNextSeat(player);

        if(nextSeat != null){

            if(nextSeat instanceof WheelSeat wheelSeat){
                wheelSeat.mountSeat(player);
            }

            if(nextSeat instanceof CannonSeat cannonSeat){
                cannonSeat.mountSeat(player);
            }
        }
    }

    public void changeHeadPoseRotation(double degrees){
        ArmorStand stand = this.invisibleVehicle.getStand();

        EulerAngle pose = stand.getHeadPose();
        pose = pose.setY(pose.getY() + Math.toRadians(degrees)); // if degrees is actually in degrees

        stand.setHeadPose(pose);
    }

    public void createVehicle() {
       // org.exampl.vehicles.Vehicle.VehiclesList.getVehiclesList().addVehicleToList(this.invisibleVehicle.getStand().getUniqueId(), this);
        org.exampl.vehicles.Vehicle.Block block = new org.exampl.vehicles.Vehicle.Block(new Vector(1, 0, 0), Material.OAK_WOOD, this.invisibleVehicle.getStand());
        this.blocks.add(block);
        org.exampl.vehicles.Vehicle.Block block2 = new org.exampl.vehicles.Vehicle.Block(new Vector(2, 0, 0), Material.OAK_WOOD, this.invisibleVehicle.getStand());
        this.blocks.add(block2);
        player.getInventory().setHeldItemSlot(0);
    }

    private void createBlocks(List<RelativeBlock> blocks) {

        this.armorStands.add(this.invisibleVehicle.getStand());

        for (RelativeBlock block : blocks) {

            BlockData blockData = Bukkit.createBlockData(block.getBlockData());

            if (blockData.getMaterial().equals(Material.AIR)) {
                continue;
            }

            org.exampl.vehicles.Vehicle.Block blockDisplay = new org.exampl.vehicles.Vehicle.Block(new Vector(block.getX(), block.getY(),block.getZ()), blockData.getMaterial(),  this.invisibleVehicle.getStand());

            this.blocks.add(blockDisplay);
        }

        player.getInventory().setHeldItemSlot(0);
    }

    private void createArmorStands(List<ArmorStandData> armorStands) {

        World world = this.player.getWorld();

        for (ArmorStandData standData: armorStands) {

            Location spawnLocation = this.invisibleVehicle.getStand().getLocation().clone().add(standData.getOffsetPosition());

           ArmorStand armorStand =  world.spawn(spawnLocation, ArmorStand.class);

            armorStand.setRotation(standData.getYaw(), standData.getPitch());
           // armorStand.setRotation(getCannonRotationYaw(standData.getYaw()), 0);
            armorStand.setInvisible(standData.isInvisible());
            armorStand.setMarker(standData.isMarker());
            armorStand.setSmall(standData.isSmall());
            armorStand.setArms(standData.hasArms());
            armorStand.setBasePlate(standData.hasBasePlate());
            armorStand.setGravity(false);


            NamespacedKey key = new NamespacedKey(Vehicles.getVehicles(), "ship_part");

            String shipPart = standData.getPartType();

            if (shipPart != null) {

                armorStand.getPersistentDataContainer().set(
                        key,
                        PersistentDataType.STRING,
                        standData.getPartType()
                );
            }

            Cannon cannon = new Cannon(armorStand, standData);
            cannons.add(cannon);
            this.armorStands.add(armorStand);
            this.seats.add(new CannonSeat(armorStand));
        }
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
            createArmorStands(ship.getArmorStands());
            org.exampl.vehicles.Vehicle.VehiclesList.getVehiclesList().addVehicleToList(this.armorStands, this);


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

    private void deleteCannonsInVehicle(){
        for(Cannon cannon : this.cannons){
           cannon.removeCannon();
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


    private void renderCannons() {

        for (Cannon cannon : cannons) {

            Vector offset =  cannon.getArmorStandData().getOffsetPosition();

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

            ArmorStand armorStand = cannon.getArmorStand();

            //armorStand.teleport(target);

            Location base = invisibleVehicle.getStand().getLocation().clone()
                    .add(0, invisibleVehicle.getStand().getHeight() / 2.0, 0);

            // Body yaw (world)
            float bodyYaw = base.getYaw();

// Head pose yaw (local, radians)
            double headYawRad = invisibleVehicle.getStand().getHeadPose().getY();

// Combine
            double finalYawRadBeforeCannon = Math.toRadians(bodyYaw) + headYawRad;

            double yawOfCannonStand = cannon.getCannonFacingYawAdjust();

            double finalYawRad = Math.toRadians(yawOfCannonStand) + finalYawRadBeforeCannon;

            float yawDegT = (float) Math.toDegrees(finalYawRad);

           //armorStand.setRotation(yawDegT, 0f);



            CraftArmorStand craftStand = (CraftArmorStand) armorStand;
            net.minecraft.world.entity.decoration.ArmorStand nms = craftStand.getHandle();


            nms.absMoveTo(
                    target.getX(),
                    target.getY(),
                    target.getZ(),
                    yawDegT,
                    0f
            );



           cannon.renderCannon(Math.toRadians(armorStand.getYaw()), armorStand.getLocation());
        }


    }

    public void startMovementLoop() {

       vehicleTask = new BukkitRunnable() {
            @Override
            public void run() {

                if (invisibleVehicle.getStand().isDead()) {

                    cancel();
                    invisibleVehicle.getStand().remove();
                    deleteBlocksInVehicle();
                    deleteCannonsInVehicle();

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


                //The blocks do not spawn in the right way before render blocks
                //The armor stand rotation might always be set 1 way. set
                // to the way the player will be looking
                renderBlocks();
                renderCannons();

            }
        }.runTaskTimer(Vehicles.getVehicles(), 1L, 1L); //was 1
    }


    private float getCannonRotationYaw(double standRotationOffset){

        Location base = invisibleVehicle.getStand().getLocation().clone()
                .add(0, invisibleVehicle.getStand().getHeight() / 2.0, 0);

        // Body yaw (world)
        float bodyYaw = base.getYaw();

// Head pose yaw (local, radians)
        double headYawRad = invisibleVehicle.getStand().getHeadPose().getY();

// Combine
        double finalYawRadBeforeCannon = Math.toRadians(bodyYaw) + headYawRad;

        double finalYawRad = Math.toRadians(standRotationOffset) + finalYawRadBeforeCannon;

        return (float) Math.toDegrees(finalYawRad);
    }

    public void deleteVehicle(){

        vehicleTask.cancel();
        invisibleVehicle.getStand().remove();
        deleteBlocksInVehicle();
        deleteCannonsInVehicle();

        VehiclesList.getVehiclesList()
                .removeVehicleFromList(invisibleVehicle.getStand().getUniqueId());

        HotbarSnapshotDatabase.getHotbarSnapshotDatabase()
                .restorePlayerHotbarSnapshot(player);

        HotbarSnapshotDatabase.getHotbarSnapshotDatabase()
                .removePlayerSnapshotFromDatabase(player);
    }

        }