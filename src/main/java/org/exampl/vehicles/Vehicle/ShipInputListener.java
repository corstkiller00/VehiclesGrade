package org.exampl.vehicles.Vehicle;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShipInputListener implements Listener {

    @EventHandler
    public void onItemSelect(PlayerItemHeldEvent event) {

        if (!(event.getPlayer().getVehicle() instanceof ArmorStand)) {
            return;
        }


        ItemStack itemStack = event.getPlayer().getInventory().getItem(event.getNewSlot());

        ItemMeta itemMeta = null;

        if(itemStack != null){
            itemMeta = itemStack.getItemMeta();
        }


        if(itemMeta != null) {
            if (itemMeta.getLore() != null) {
                String lore = itemMeta.getLore().getFirst();
                loreToMotion(lore, (ArmorStand) event.getPlayer().getVehicle(), event.getPlayer());
            }
        }

        event.getPlayer().getInventory().setHeldItemSlot(0);

    }

    private void loreToMotion(String lore, ArmorStand armorStand, Player player){

        Vehicle vehicle = VehiclesList.getVehiclesList().getVehicleFromList(armorStand.getUniqueId());

        if(vehicle == null){
            return;
        }

        switch(lore) {
            case "§6Adjust Sails":
                if(vehicle.isSailsDown()){
                    vehicle.setSailsDown(false);
                    vehicle.getPlayer().sendActionBar(
                            Component.text("⚓ Sails Raised!", NamedTextColor.GOLD)
                    );
                }else{
                    vehicle.setSailsDown(true);
                    vehicle.getPlayer().sendActionBar(
                            Component.text("⚓ Full Sails Ahead!", NamedTextColor.GOLD)
                    );
                }
// Statements
                break;
            case "§6<--- Port":
                vehicle.changeHeadPoseRotation(-5.0);
// Statements
                break;
            case "§6---> Starboard":
                vehicle.changeHeadPoseRotation(5.0);
// Statements
                break;

            case "§6Anchor":
                if(vehicle.isAnchored()){
                    vehicle.setAnchored(false);
                    player.playSound(
                            player.getLocation(),
                            Sound.BLOCK_CHAIN_PLACE,
                            0.8f,
                            0.9f
                    );
                    vehicle.getPlayer().sendActionBar(
                            Component.text("⚓ Anchor up!", NamedTextColor.GOLD)
                    );
                }else{
                    vehicle.setAnchored(true);
                    player.playSound(
                            player.getLocation(),
                            Sound.BLOCK_ANVIL_LAND,
                            1.2f,
                            0.65f
                    );
                    vehicle.getPlayer().sendActionBar(
                            Component.text("⚓ Drop Anchor!", NamedTextColor.GOLD)
                    );
                }
// Statements
                break;
// You can have any number of case statements
            default:
// Default statements
        }
    }
}

