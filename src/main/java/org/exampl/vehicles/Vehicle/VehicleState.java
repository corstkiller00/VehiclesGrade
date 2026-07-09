package org.exampl.vehicles.Vehicle;

import org.bukkit.entity.ArmorStand;
import org.exampl.vehicles.ShipCreator.PasteShip;
import org.exampl.vehicles.ShipCreator.ShipPastedData;
import org.exampl.vehicles.ShipCreator.ShipStructure;

public class VehicleState {

    private boolean isParked;
    private ShipStructure shipStructure;
    private Vehicle vehicle;
    private ArmorStand wheel;


    public VehicleState(boolean isParked, ShipStructure shipStructure, Vehicle vehicle, ArmorStand wheel) {
        this.isParked = isParked;
        this.shipStructure = shipStructure;
        this.vehicle = vehicle;
        this.wheel = wheel;
    }

    public boolean isParked() {
        return isParked;
    }

    public void setParked(boolean parked) {
        isParked = parked;
    }


    public void parkShip(){

        //Ensure motion of ship is 0.0
        setMotionOfShipTo0();

        //Create ship out of solid blocks
        createSolidShip();

        //Save these blocks somewhere
        //destroy the unparked ship
        //Remove player from passenger seat if needed

    }

    private void setMotionOfShipTo0(){
        vehicle.setAcceleration(0.0);
        vehicle.setCurrentSpeed(0.0);
    }

    private void createSolidShip(){

        ShipPastedData shipPastedData =  new PasteShip().pasteShip(wheel.getLocation(), this.shipStructure);
    }


    public void unParkShip(){

        //Crate ship out of Block Displays
        //include armor stands
        //Put player in passenger seat
        //Remove solid blocks
    }
}
