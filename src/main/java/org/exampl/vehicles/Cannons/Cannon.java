package org.exampl.vehicles.Cannons;

import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.exampl.vehicles.ShipCreator.ArmorStandData;
import org.exampl.vehicles.ShipCreator.ShipPartType;
import org.exampl.vehicles.Vehicle.Block;
import org.exampl.vehicles.Vehicles;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Cannon {

    private ArmorStand armorStands;
    private ArmorStandData armorStandData;
    private BlockDisplay blastFurnance;
    private ItemDisplay barrel;
    private Interaction pivot;
    private Quaternionf initialRotation;
    private float cannonYaw;
    private float cannonPitch;
    private final ArmorStand stand;
    private Vector furnanceOffset;
    private float cannonFacingYawAdjust;
    private boolean rotationBarrelSet = false;


    public ArmorStand getArmorStands() {
        return armorStands;
    }

    public ArmorStand getArmorStand() {
        return stand;
    }

    public float getCannonFacingYawAdjust() {
        return cannonFacingYawAdjust;
    }

    public ItemDisplay getBarrel() {
        return barrel;
    }

    public BlockDisplay getBlastFurnance() {
        return blastFurnance;
    }

    public ArmorStandData getArmorStandData() {
        return armorStandData;
    }

    public Cannon(ArmorStand stand, ArmorStandData armorStandData) {

        this.stand = stand;
        CannonManager.getCannonManager().addCannonToManager(this.stand.getUniqueId(), this);

        this.armorStandData = armorStandData;

        //Get the direction to face the furnace



        BlockFace face = getClosestFacing(stand.getLocation().getYaw());

        cannonFacingYawAdjust = getYaw(face);

        //Set the stand to the neared NESW direction

        this.stand.setRotation(getYaw(face), 0f);

        Location cannonLocation = this.stand.getLocation();


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
        float yaw = this.stand.getLocation().getYaw();


        Location baseLoc = cannonLocation.clone();
        baseLoc.setYaw(yaw);
        baseLoc.setPitch(0);

        /*
        // Create the parent Armor stand
         world.spawn(baseLoc, ArmorStand.class, as -> {
            as.setInvisible(false);
            as.setMarker(true);
            as.setGravity(false);
            as.setInvulnerable(true);
            as.setRotation(yaw, 0);
        });

         */

        //Give stand the cannon ship part tag.
        //setPersistentDataContainer();


        // Base (Blast Furnace)
        blastFurnance = world.spawn(furnaceLoc, BlockDisplay.class, bd -> {
            Directional furnace = (Directional) Bukkit.createBlockData(Material.BLAST_FURNACE);

            //furnace.setFacing(
                  //  getClosestFacing(this.stand.getLocation().getYaw())
            furnace.setFacing(getFurnanceFacingDirection());


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
                            .rotateY((float) Math.toRadians(-stand.getYaw()))

                            // Lay the lightning rod like a barrel
                            .rotateX((float) Math.toRadians(90)),

                    // Make it longer
                    new Vector3f(1f, 2.2f, 1f),

                    new Quaternionf()
            );

            id.setTransformation(t);

        });

        blastFurnance.setInterpolationDuration(2);  //was 2
        blastFurnance.setInterpolationDelay(0);
        blastFurnance.setTeleportDuration(2); //was 2

        barrel.setInterpolationDuration(2);
        barrel.setInterpolationDelay(0);
        barrel.setTeleportDuration(2);

        // testShoot();
      //  rotateCannon(player);
    }

    public Cannon(Location cannonLocation, Player player) {

        //Get the direction to face the furnace

        BlockFace face = getClosestFacing(player.getLocation().getYaw());

        //Define the forward vector from the face of the furnace

        Vector forward = new Vector(
                face.getModX(),
                0,
                face.getModZ()
        );

        Vector direction = player.getLocation().getDirection();


        Location furnaceLoc = cannonLocation.clone()
                .add(direction.clone().multiply(1))
                .add(0, 0, 0);     //Raise the height 0.45 from armor stand base


        furnaceLoc.setYaw(0);
        furnaceLoc.setPitch(0);

        Location barrelLoc = furnaceLoc.clone()
                .add(direction.clone().multiply(1.20))
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

        //Give stand the cannon ship part tag.
        setPersistentDataContainer();


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
                            .rotateY((float) Math.toRadians(-stand.getYaw()))

                            // Lay the lightning rod like a barrel
                            .rotateX((float) Math.toRadians(90)),

                    // Make it longer
                    new Vector3f(1f, 2.2f, 1f),

                    new Quaternionf()
            );

            id.setTransformation(t);
        });



       // testShoot();
        //rotateCannon(player);
    }

    public void shoot() {


        // Direction the cannon is pointing
       // Vector direction = barrel.getLocation().getDirection();

        Vector direction = getDirectionFromYawPitch(cannonYaw, cannonPitch);

        // Spawn the cannonball slightly in front of the barrel
        Location muzzle = barrel.getLocation().clone()
                .add(direction.multiply(1.5))
                .add(-0.5, 0.0, -0.5);

        effects(muzzle);

        new CannonBall(muzzle, direction);
    }

    private void effects(Location spawn) {

        World world = spawn.getWorld();

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

                if (!stand.getPassengers().contains(player)) {
                    cancel();
                    return;
                }

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
                    cannonYaw = baseYaw + clampedRelativeYaw;


                    // Limit cannon elevation
                    float pitch = player.getLocation().getPitch();
                    cannonPitch = Math.max(-20, Math.min(20, pitch));

                    Quaternionf rotation = new Quaternionf()
                            // Turn cannon to player direction
                            .rotateY((float) Math.toRadians(-cannonYaw))

                            // Lay the lightning rod like a barrel
                            .rotateX((float) Math.toRadians(90))

                            // Aim slightly up/down
                            .rotateX((float) Math.toRadians(cannonPitch));


                    barrel.setTransformation(new Transformation(
                            new Vector3f(0f, 0f, 0f),
                            rotation,
                            new Vector3f(1f, 2.2f, 1f),
                            new Quaternionf()
                    ));

                }

        }.runTaskTimer(Vehicles.getVehicles(), 1L, 1L);
    }


    public void playerUsingStand(Player player){
        stand.addPassenger(player);
        rotateCannon(player);
    }

    public void renderCannon(double cachedYaw, Location cachedCentre) {

        //Do the furnance first

            Vector forward = new Vector(
                    -Math.sin(cachedYaw),
                    0,
                    Math.cos(cachedYaw)
            ).normalize();

            Location target = cachedCentre.clone().add(forward);

            blastFurnance.teleport(target);

            //Do the Barrel



            Transformation t = new Transformation(

                    new Vector3f(0f, 0f, 0f),

                    // Rotate onto its side
                    new Quaternionf()
                            // Turn cannon to player direction
                            .rotateY((float) Math.toRadians(-stand.getYaw()))

                            // Lay the lightning rod like a barrel
                            .rotateX((float) Math.toRadians(90)),

                    // Make it longer
                    new Vector3f(1f, 2.2f, 1f),

                    new Quaternionf()
            );

            barrel.setTransformation(t);


        Vector front = new Vector(
                -Math.sin(cachedYaw),
                0,
                Math.cos(cachedYaw)
        ).normalize();



        Location barrelTarget = target.clone()
                .add(front.clone().multiply(1.20))
                .add(0, 0.5, 0);

        barrelTarget.setYaw(0f);
        barrelTarget.setPitch(0f);

        barrel.teleport(barrelTarget);


        //Need to have a look at spanwing the armor stand in the correct place on the
        //ship in first place to make this easier.
        //Also an odd angle on the barrel.


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
            case WEST -> 90f;
            case NORTH -> 180f;
            case EAST -> -90f; // or 270f
            default -> 0f;
        };
    }


    private BlockFace getFurnanceFacingDirection(){
        double yaw = this.stand.getYaw();

        //Only does port and starboard right now

        if (yaw >= 45 && yaw < 135) {
            return BlockFace.SOUTH;
        }else{
            return BlockFace.NORTH;
        }
    }

    private void testRotation(BlockDisplay blockDisplay) {



        new BukkitRunnable() {
            float rotation = 0.0f;
            @Override
            public void run() {

                if(rotation > 360.0f){
                    rotation = 1.0f;
                }

                Transformation t = new Transformation(
                        new Vector3f(-0.5f, 0f, -0.5f),  //offset of the 0,0 corner.
                        new Quaternionf()
                                .rotationZ(rotation),
                        new Vector3f(1f, 1f, 1f),
                        new Quaternionf()
                );

                blockDisplay.setTransformation(t);

                rotation = rotation + 1;

            }
        }.runTaskTimer(Vehicles.getVehicles(), 1L, 1L);
    }


    private void testShoot() {



        new BukkitRunnable() {
            @Override
            public void run() {

              shoot();
            }
        }.runTaskTimer(Vehicles.getVehicles(), 1L, 60L);
    }


    private Vector getDirectionFromYawPitch(float yaw, float pitch) {
        double yawRad = Math.toRadians(yaw);
        double pitchRad = Math.toRadians(pitch);

        double x = -Math.sin(yawRad) * Math.cos(pitchRad);
        double y = -Math.sin(pitchRad);
        double z = Math.cos(yawRad) * Math.cos(pitchRad);

        return new Vector(x, y, z);
    }


    private void setPersistentDataContainer() {
        NamespacedKey key = new NamespacedKey(Vehicles.getVehicles(), "ship_part");

        stand.getPersistentDataContainer().set(
                key,
                PersistentDataType.STRING,
                ShipPartType.CANNON.name()
        );
    }

    public void removeCannon(){
        this.stand.remove();
        this.blastFurnance.remove();
        this.barrel.remove();
    }
}