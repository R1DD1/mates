package me.moteloff.mates.main.managers;

import me.moteloff.mates.main.Main;
import me.moteloff.mates.main.event.Difficulty;
import me.moteloff.mates.main.event.Location;
import me.moteloff.mates.main.event.events.MobArena;
import org.bukkit.Bukkit;

public class EventManager implements Manager{


    @Override
    public void register() {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), ()-> {
            Main.activeEvent = new MobArena(Difficulty.HARD, Location.WINTER);
            Main.activeEvent.register();
        }, 100L);
    }

    @Override
    public void unregister() {
//        Main.activeEvent.unregister();
    }
}
