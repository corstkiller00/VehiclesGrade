package org.exampl.vehicles.Vehicle;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HotbarSnapshotDatabase {

    private static HotbarSnapshotDatabase hotbarSnapshotDatabase;
    Map<UUID, org.exampl.vehicles.Vehicle.HotbarSnapshot> savedHotbars = new HashMap<>();

    private HotbarSnapshotDatabase(){}

    public void addPlayerSnapshotToDatabase(Player player, org.exampl.vehicles.Vehicle.HotbarSnapshot snapshot) {
        savedHotbars.put(player.getUniqueId(), snapshot);
    }

    public void removePlayerSnapshotFromDatabase(Player player) {
        savedHotbars.remove(player.getUniqueId());
    }

    public void restorePlayerHotbarSnapshot(Player player){
        savedHotbars.get(player.getUniqueId()).restore();
    }

    public static HotbarSnapshotDatabase getHotbarSnapshotDatabase() {
        if(HotbarSnapshotDatabase.hotbarSnapshotDatabase == null){
            hotbarSnapshotDatabase = new HotbarSnapshotDatabase();
        }
        return hotbarSnapshotDatabase;
    }
}
