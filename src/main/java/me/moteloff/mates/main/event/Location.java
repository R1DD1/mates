package me.moteloff.mates.main.event;

import me.moteloff.mates.main.Main;
import me.moteloff.mates.main.utils.Formatting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public enum Location {
    WINTER_MOB(Formatting.translate(Main.getInstance().getEventCfg().getString("mob_arena.location.winter")), new org.bukkit.Location(Bukkit.getWorld("world"), 0, 270, 3000)),
    JUNGLE_MOB(Formatting.translate(Main.getInstance().getEventCfg().getString("mob_arena.location.jungle")), new org.bukkit.Location(Bukkit.getWorld("world"), 100, 270, 3000)),
    WATER_MOB(Formatting.translate(Main.getInstance().getEventCfg().getString("mob_arena.location.water")), new org.bukkit.Location(Bukkit.getWorld("world"), 200, 270, 3000)),
    FIRE_MOB(Formatting.translate(Main.getInstance().getEventCfg().getString("mob_arena.location.fire")), new org.bukkit.Location(Bukkit.getWorld("world"), 300, 270, 3000)),
    SKY_MOB(Formatting.translate(Main.getInstance().getEventCfg().getString("mob_arena.location.sky")), new org.bukkit.Location(Bukkit.getWorld("world"), 400, 270, 3000)),
    ROOM_QUIZ(Formatting.translate(Main.getInstance().getEventCfg().getString("mob_arena.location.winter")), new org.bukkit.Location(Bukkit.getWorld("world"), 0, 270, 3000));
    private String title;
    private org.bukkit.Location location;

    Location(String title, org.bukkit.Location location) {
        this.title = title;
        this.location = location;
    }

    public String getTitle(){
        return title;
    }

    public org.bukkit.Location getLocation() {
        return location;
    }

    public void teleport(List<Player> players) {
        org.bukkit.Location location = getLocation();
        for (Player player: players) {player.teleport(location);}
    }
}
