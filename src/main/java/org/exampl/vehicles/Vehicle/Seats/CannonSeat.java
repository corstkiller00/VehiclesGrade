package org.exampl.vehicles.Vehicle.Seats;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.exampl.vehicles.Cannons.CannonManager;
import org.exampl.vehicles.Vehicle.Vehicle;
import org.exampl.vehicles.helper.MountManager;

public class CannonSeat extends Seat {


    public CannonSeat(ArmorStand armorStand) {
        super(armorStand);
    }

    @Override
    public void mountSeat(Player player) {
        super.placePlayerOnStand(player);

        org.exampl.vehicles.Cannons.Cannon cannon = CannonManager.getCannonManager().getCannonFromManager(super.getStand().getUniqueId());

        if(cannon != null){
            MountManager.getMountManager().addPlayerToManager(player);
            MountManager.getMountManager().removePlayerFromManagerInOneTick(player);
            cannon.playerUsingStand(player);
        }
    }
    }


