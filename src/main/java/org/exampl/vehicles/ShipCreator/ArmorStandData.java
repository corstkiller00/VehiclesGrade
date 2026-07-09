package org.exampl.vehicles.ShipCreator;

public class ArmorStandData {

        private double x;
        private double y;
        private double z;

        private float yaw;
        private float pitch;

        private String customName;

        private boolean invisible;
        private boolean marker;
        private boolean small;
        private boolean arms;
        private boolean basePlate;
        private String partType;

    public ArmorStandData(double x, double y, double z, float yaw, float pitch, String customName, boolean invisible, boolean marker, boolean small, boolean arms, boolean basePlate) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.customName = customName;
        this.invisible = invisible;
        this.marker = marker;
        this.small = small;
        this.arms = arms;
        this.basePlate = basePlate;

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public String getCustomName() {
        return customName;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public boolean isMarker() {
        return marker;
    }

    public boolean isSmall() {
        return small;
    }

    public boolean hasArms() {
        return arms;
    }

    public boolean hasBasePlate() {
        return basePlate;
    }

    public String getPartType(){
        return this.partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }
}

