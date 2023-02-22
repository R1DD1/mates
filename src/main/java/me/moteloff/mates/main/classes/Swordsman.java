package me.moteloff.mates.main.classes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Swordsman implements RPGClass{
    @Override
    public void onChoice(Player player) {
        //Что происходит при принятии класс Swordsman
    }

    @Override
    public void onHit(Player player, LivingEntity entity) {
        //Что происходит при ударе игрока с классом Swordsman
    }

    @Override
    public void onDamaged(Player player, LivingEntity entity) {
        //Что происходит при ударе по игроку с классом Swordsman
    }

    @Override
    public void onDeath(Player player) {
        //Что происходит когда игрока с классом Swordsman умирает
    }

    @Override
    public void onKill(Player player) {
        //Что происходит когда игрока с классом Swordsman убивает
    }

}
