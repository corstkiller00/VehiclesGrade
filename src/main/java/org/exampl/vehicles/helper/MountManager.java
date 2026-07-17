package org.exampl.vehicles.helper;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.exampl.vehicles.Cannons.Cannon;
import org.exampl.vehicles.Vehicles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MountManager {


    private static MountManager mountManager;
    private ArrayList<Player> playersOnMounts = new ArrayList<>();

    private MountManager(){}

    public static MountManager getMountManager() {
        if(MountManager.mountManager == null){
           mountManager = new MountManager();
        }
        return mountManager;
    }

    public void addPlayerToManager(Player player){
        playersOnMounts.add(player);
    }

    public void removePlayerFromManager(Player player){
        playersOnMounts.remove(player);
    }

    public void removePlayerFromManagerInOneTick(Player player){
        Bukkit.getScheduler().runTaskLater(Vehicles.getVehicles(), () -> {
            removePlayerFromManager(player);
        }, 5L);
    }

    public boolean hasPlayerGotOnMount(Player player){
        return playersOnMounts.contains(player);
    }
}
