package me.moteloff.mates.main.managers;

import me.moteloff.mates.main.Main;
import org.bukkit.Bukkit;

public interface Manager {

    default void register() {}

    default  void unregister() {

    }
}
