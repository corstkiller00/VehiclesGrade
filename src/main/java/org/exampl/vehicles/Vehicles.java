package org.exampl.vehicles;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.exampl.vehicles.Commands.LoadShipCommand;
import org.exampl.vehicles.Commands.SaveShipCommand;
import org.exampl.vehicles.Commands.SpawnBoatCommand;
import org.exampl.vehicles.Database.DatabaseManager;
import org.exampl.vehicles.Listener.SneakListener;
import org.exampl.vehicles.Vehicle.InteractionTask;
import org.exampl.vehicles.Vehicle.ShipInputListener;

import java.sql.SQLException;

public final class Vehicles extends JavaPlugin {

    private static Vehicles vehicles;
    private DatabaseManager database;

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    @Override
    public void onEnable() {
        // Plugin startup logic
        Vehicles.vehicles = this;
        connectDatabase();
        getServer().getPluginManager().registerEvents(new ShipInputListener(), this);
        getServer().getPluginManager().registerEvents(new SneakListener(), this);
        getCommand("spawnboat").setExecutor(new SpawnBoatCommand());
        getCommand("spawnboat").setTabCompleter(new SpawnBoatCommand());
        getCommand("saveship").setExecutor(new SaveShipCommand());
        getCommand("saveship").setTabCompleter(new SaveShipCommand());
        getCommand("loadship").setExecutor(new LoadShipCommand());
        getCommand("loadship").setTabCompleter(new LoadShipCommand());
        new InteractionTask().runTaskTimer(this, 0L, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        database.close();
    }

    private void connectDatabase(){

        database = new DatabaseManager(this);

        try {
            database.connect();
        } catch (SQLException e) {
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public static Vehicles getVehicles() {
        return vehicles;
    }

    public Gson getGson() {
        return gson;
    }
}
