package org.exampl.vehicles.ShipCreator;

public class RelativeBlock {

    private final int x;
    private final int y;
    private final int z;
    private final String blockData;

    public RelativeBlock(int x, int y, int z, String blockData) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockData = blockData;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getBlockData() {
        return blockData;
    }
}