package me.moteloff.mates.main;

import me.moteloff.mates.main.commands.Event;
import me.moteloff.mates.main.commands.Login;
import me.moteloff.mates.main.database.DatabaseConstructor;
import me.moteloff.mates.main.listeners.CanceledActions;
import me.moteloff.mates.main.listeners.InventoryListener;
import me.moteloff.mates.main.listeners.JoinListener;
import me.moteloff.mates.main.listeners.VkListener;
import me.moteloff.mates.main.event.Difficulty;
import me.moteloff.mates.main.event.Location;
import me.moteloff.mates.main.event.events.MobArena;
import me.moteloff.mates.main.managers.EventManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class Main extends JavaPlugin {

    private static Main instance;
    private final DatabaseConstructor databaseConstructor = DatabaseConstructor.INSTANCE;

    public static me.moteloff.mates.main.event.Event activeEvent;
    public static Inventory eventInv;
    public static ItemStack eventItem;

    private final File configurationFile = new File(getDataFolder(), "event.yml");
    private final FileConfiguration configuration = YamlConfiguration.loadConfiguration(configurationFile);

    private final EventManager eventManager = new EventManager();


    @Override
    public void onEnable() {
        instance = this;

        eventManager.register();

        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new VkListener(), this);
        getServer().getPluginManager().registerEvents(new CanceledActions(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        databaseConstructor.createTable();

        Objects.requireNonNull(getServer().getPluginCommand("login")).setExecutor(new Login());
        Objects.requireNonNull(getServer().getPluginCommand("event")).setExecutor(new Event());

        eventInv = Bukkit.createInventory(null, 9, Component.text(" "));
    }

    @Override
    public void onDisable() {eventManager.unregister();}

    public static Main getInstance() { return instance; }

    public FileConfiguration getEventCfg() { return configuration; }
}
