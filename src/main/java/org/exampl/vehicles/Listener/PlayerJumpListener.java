package org.exampl.vehicles.Listener;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInputEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.exampl.vehicles.Cannons.CannonManager;
import org.exampl.vehicles.ShipCreator.ShipPartType;
import org.exampl.vehicles.Vehicles;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerJumpListener implements Listener {

    private final NamespacedKey partKey;
    //private final Map<UUID, Boolean> lastJumpState = new HashMap<>();
    private final Map<UUID, Boolean> hasPlayerShot = new HashMap<>();


    public PlayerJumpListener() {
        this.partKey = new NamespacedKey(Vehicles.getVehicles(), "ship_part");
    }

    @EventHandler
    public void onPlayerJump(PlayerInputEvent event) {

        if(event.getPlayer().getVehicle() instanceof ArmorStand stand){
            Player player = event.getPlayer();

            boolean jumping = event.getInput().isJump();
            if(!jumping){
                return;
            }

            PersistentDataContainer pdc = stand.getPersistentDataContainer();


            if (!pdc.has(partKey, PersistentDataType.STRING)) {
                System.out.println("True");
                return;
            }

            String part = pdc.get(partKey, PersistentDataType.STRING);


            if(part == null){
                return;
            }

            if (part.equals(ShipPartType.CANNON.name())) {
                cannonInteract(stand, player);
            }

        }
    }

    private void cannonInteract(ArmorStand stand, Player player){

        //If player has not shot recently shoot and set to shot recently.
        //Set timer for removing shot recently status for player

        if(hasPlayerShot.getOrDefault(player.getUniqueId(), false) == false){
            org.exampl.vehicles.Cannons.Cannon cannon = CannonManager.getCannonManager().getCannonFromManager(stand.getUniqueId());
            cannon.shoot();
            hasPlayerShot.put(player.getUniqueId(), true);
            setCannonCooldown(player);
        }

    }

    private void setCannonCooldown(Player player){

        //After time set the hasplayershot back to false
        Bukkit.getScheduler().runTaskLater(Vehicles.getVehicles(), () -> {
            hasPlayerShot.put(player.getUniqueId(), false);
        }, 5L * 20L);
    }
}
