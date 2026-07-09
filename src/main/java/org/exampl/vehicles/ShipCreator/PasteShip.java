package org.exampl.vehicles.ShipCreator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.persistence.PersistentDataType;
import org.exampl.vehicles.Vehicles;

import java.util.ArrayList;

public class PasteShip {

    public ShipPastedData pasteShip(Location origin, ShipStructure ship) {

        return new ShipPastedData(pasteBlocks(origin, ship),  pasteArmorStands(origin, ship));

        }


        private ArrayList<Block> pasteBlocks(Location origin, ShipStructure ship){

         ArrayList<Block> blocks = new ArrayList<>();

            for (RelativeBlock saved : ship.getBlocks()) {

                Block block = origin.getWorld().getBlockAt(
                        origin.getBlockX() + saved.getX(),
                        origin.getBlockY() + saved.getY(),
                        origin.getBlockZ() + saved.getZ()
                );

                block.setBlockData(Bukkit.createBlockData(saved.getBlockData()));
            }

            return blocks;

        }

        private ArrayList<ArmorStand> pasteArmorStands(Location origin, ShipStructure ship){

        ArrayList<ArmorStand> armorStands = new ArrayList<>();

            for(ArmorStandData data : ship.getArmorStands()) {

                Location spawn = origin.clone().add(
                        data.getX(),
                        data.getY(),
                        data.getZ()
                );

                ArmorStand stand = spawn.getWorld().spawn(spawn, ArmorStand.class);

                stand.setRotation(data.getYaw(), data.getPitch());
                stand.setCustomName(data.getCustomName());
                stand.setInvisible(data.isInvisible());
                stand.setMarker(data.isMarker());
                stand.setSmall(data.isSmall());
                stand.setArms(data.hasArms());
                stand.setBasePlate(!data.hasBasePlate());

                NamespacedKey key = new NamespacedKey(Vehicles.getVehicles(), "ship_part");

                String shipPart = data.getPartType();

                if (shipPart != null) {

                    stand.getPersistentDataContainer().set(
                            key,
                            PersistentDataType.STRING,
                            data.getPartType()
                    );
                }

                armorStands.add(stand);
            }

            return armorStands;
        }
    }



