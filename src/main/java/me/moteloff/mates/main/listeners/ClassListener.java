package me.moteloff.mates.main.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ClassListener implements Listener {

    @EventHandler
    public void onClassSwitch(PlayerSwitchClassEvent e) {
        System.out.println("Lox");
    }
}
