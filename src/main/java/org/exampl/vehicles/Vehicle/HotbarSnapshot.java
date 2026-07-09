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

        for (int i = 0; i < 9; i++) {
            ItemStack item = items[i];

            if (item == null) {
                System.out.println("Slot " + i + ": null");
            } else {
                System.out.println("Slot " + i + ": "
                        + item.getType()
                        + " x" + item.getAmount());
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


        ItemStack wheel = new ItemStack(Material.COMPASS);
        ItemMeta SteerLeftMeta = wheel.getItemMeta();
        Component.text("Steer Left", NamedTextColor.GOLD);
        List loreListSteerLeft = new ArrayList();
        loreListSteerLeft.add(Component.text("Steer Left", NamedTextColor.GOLD));
        SteerLeftMeta.lore(loreListSteerLeft);
        wheel.setItemMeta(SteerLeftMeta);
        player.getInventory().setItem(2, wheel);



        ItemStack steerRight = new ItemStack(Material.ARROW);
        ItemMeta SteerRightMeta = steerRight.getItemMeta();
        Component.text("Steer Right", NamedTextColor.GOLD);
        List loreListSteerRight = new ArrayList();
        loreListSteerRight.add(Component.text("Steer Right", NamedTextColor.GOLD));
        SteerRightMeta.lore(loreListSteerRight);
        steerRight.setItemMeta(SteerRightMeta);
        player.getInventory().setItem(4, steerRight);




        ItemStack lowerSails = new ItemStack(Material.WHITE_BANNER);
        ItemMeta lowerSailsMeta = lowerSails.getItemMeta();
        Component.text("Adjust Sails", NamedTextColor.GOLD);
        List loreListLowerSails = new ArrayList();
        loreListLowerSails.add(Component.text("Adjust Sails", NamedTextColor.GOLD));
        lowerSailsMeta.lore(loreListLowerSails);
        lowerSails.setItemMeta(lowerSailsMeta);
        player.getInventory().setItem(6, lowerSails);



        ItemStack anchor = new ItemStack(Material.CHAIN);
        ItemMeta anchorMeta = anchor.getItemMeta();
        Component.text("Anchor", NamedTextColor.GOLD);
        List loreListAnchor = new ArrayList();
        loreListAnchor.add(Component.text("Anchor", NamedTextColor.GOLD));
        anchorMeta.lore(loreListAnchor);
        anchor.setItemMeta(anchorMeta);
        player.getInventory().setItem(8, anchor);
    }
}