package org.exampl.vehicles.Vehicle.Seats;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.exampl.vehicles.Vehicle.Vehicle;

public class WheelSeat extends Seat {

    public WheelSeat(ArmorStand armorStand) {
        super(armorStand);
    }

    @Override
    public void mountSeat(Player player) {
        super.placePlayerOnStand(player);

        //Below is to give the player the required wheel controls
        org.exampl.vehicles.Vehicle.HotbarSnapshot snapshot = new org.exampl.vehicles.Vehicle.HotbarSnapshot(player, true);
        org.exampl.vehicles.Vehicle.HotbarSnapshotDatabase.getHotbarSnapshotDatabase().addPlayerSnapshotToDatabase(player, snapshot);
        snapshot.setShipHotbar();

    }

}
