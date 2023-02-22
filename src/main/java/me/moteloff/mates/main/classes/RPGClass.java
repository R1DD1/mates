package me.moteloff.mates.main.classes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface RPGClass {

    default void onChoice(Player player) {}
    default void onHit(Player player, LivingEntity entity){}
    default void onDamaged(Player player, LivingEntity entity){}
    default void onDeath(Player player){}
    default void onKill(Player player){}
}
