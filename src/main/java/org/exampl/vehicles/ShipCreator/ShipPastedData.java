package org.exampl.vehicles.ShipCreator;

import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;

public class ShipPastedData {

    private ArrayList<Block> blocks;
    private ArrayList<ArmorStand> armorStands;

    public ShipPastedData(ArrayList<Block> blocks, ArrayList<ArmorStand> armorStands) {
        this.blocks = blocks;
        this.armorStands = armorStands;
    }
}
