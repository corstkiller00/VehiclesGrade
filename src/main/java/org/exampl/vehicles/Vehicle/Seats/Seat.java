package org.exampl.vehicles.Vehicle.Seats;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.exampl.vehicles.Vehicle.Vehicle;

public abstract class Seat {

    private int seatNumber;
    private ArmorStand stand;

    public int getSeatNumber() {
        return seatNumber;
    }

    public Seat(ArmorStand armorStand) {
       this.stand = armorStand;
    }

    public ArmorStand getStand() {
        return stand;
    }

    public Player getPlayerOnSeat() {

        if(this.stand.getPassengers().isEmpty()){
            return null;
        }else {
            return (Player) this.stand.getPassengers().getFirst();
        }
    }

    public abstract void mountSeat(Player player);

    public void placePlayerOnStand(Player player){

        player.leaveVehicle();
        stand.addPassenger(player);

    }


}
