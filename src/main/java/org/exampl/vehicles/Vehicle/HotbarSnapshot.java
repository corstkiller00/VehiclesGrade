package org.exampl.vehicles.Vehicle;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class HotbarSnapshot {

    private Player player;

    private final ItemStack[] items = new ItemStack[9];

    public HotbarSnapshot(Player player, boolean clearAfter) {

        this.player = player;

        for (int i = 0; i < 9; i++) {
            ItemStack item = player.getInventory().getItem(i);

            // clone to avoid reference issues
            items[i] = (item != null) ? item.clone() : null;
        }

        if (clearAfter) {
            for (int i = 0; i < 9; i++) {
                player.getInventory().setItem(i, null);
            }
        }
    }

    public void restore() {

        for (int i = 0; i < 9; i++) {
            player.getInventory().setItem(i, items[i]);
        }

        player.updateInventory();
    }


    public void setShipHotbar() {


        ItemStack wheel = new ItemStack(Material.SPECTRAL_ARROW);
        ItemMeta SteerLeftMeta = wheel.getItemMeta();
        SteerLeftMeta.displayName(Component.text("<--- Port", NamedTextColor.GOLD));
        List loreListSteerLeft = new ArrayList();
        loreListSteerLeft.add(Component.text("<--- Port", NamedTextColor.GOLD));
        SteerLeftMeta.lore(loreListSteerLeft);
        wheel.setItemMeta(SteerLeftMeta);
        player.getInventory().setItem(1, wheel);



        ItemStack steerRight = new ItemStack(Material.ARROW);
        ItemMeta SteerRightMeta = steerRight.getItemMeta();
        SteerRightMeta.displayName(Component.text("---> Starboard", NamedTextColor.GOLD));
        List loreListSteerRight = new ArrayList();
        loreListSteerRight.add(Component.text("---> Starboard", NamedTextColor.GOLD));
        SteerRightMeta.lore(loreListSteerRight);
        steerRight.setItemMeta(SteerRightMeta);
        player.getInventory().setItem(7, steerRight);




        ItemStack lowerSails = new ItemStack(Material.WHITE_BANNER);
        ItemMeta lowerSailsMeta = lowerSails.getItemMeta();
        lowerSailsMeta.displayName(Component.text("Adjust Sails", NamedTextColor.GOLD));
        List loreListLowerSails = new ArrayList();
        loreListLowerSails.add(Component.text("Adjust Sails", NamedTextColor.GOLD));
        lowerSailsMeta.lore(loreListLowerSails);
        lowerSails.setItemMeta(lowerSailsMeta);
        player.getInventory().setItem(3, lowerSails);



        ItemStack anchor = new ItemStack(Material.CHAIN);
        ItemMeta anchorMeta = anchor.getItemMeta();
        anchorMeta.displayName(Component.text("Anchor", NamedTextColor.GOLD));
        List loreListAnchor = new ArrayList();
        loreListAnchor.add(Component.text("Anchor", NamedTextColor.GOLD));
        anchorMeta.lore(loreListAnchor);
        anchor.setItemMeta(anchorMeta);
        player.getInventory().setItem(5, anchor);

        ItemStack switchSeat = new ItemStack(Material.IRON_INGOT);
        ItemMeta switchSeatMeta = switchSeat.getItemMeta();
        switchSeatMeta.displayName(Component.text("Switch seat", NamedTextColor.GOLD));
        List loreListSwitchSeat = new ArrayList();
        loreListSwitchSeat.add(Component.text("Switch seat", NamedTextColor.GOLD));
        switchSeatMeta.lore(loreListSwitchSeat);
        switchSeat.setItemMeta(switchSeatMeta);
        player.getInventory().setItem(8, switchSeat);
    }

    public Player getPlayer() {
        return player;
    }
}