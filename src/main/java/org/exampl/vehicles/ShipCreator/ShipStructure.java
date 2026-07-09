package org.exampl.vehicles.ShipCreator;

import java.util.ArrayList;
import java.util.List;

public class ShipStructure {

    private int formatVersion = 1;
    private String name;
    private List<org.exampl.vehicles.ShipCreator.RelativeBlock> blocks;
    private List<org.exampl.vehicles.ShipCreator.ArmorStandData> armorStands = new ArrayList<>();

    public ShipStructure(String name, List<org.exampl.vehicles.ShipCreator.RelativeBlock> blocks, List<org.exampl.vehicles.ShipCreator.ArmorStandData> armorStands) {
        this.name = name;
        this.blocks = blocks;
        this.armorStands = armorStands;
    }

    public String getName() {
        return name;
    }

    public List<org.exampl.vehicles.ShipCreator.RelativeBlock> getBlocks() {
        return blocks;
    }

    public List<org.exampl.vehicles.ShipCreator.ArmorStandData> getArmorStands() {
        return armorStands;
    }
}