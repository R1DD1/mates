package me.moteloff.mates.main.classes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Swordsman implements RPGClass{
    @Override
    public void onChoice(Player player) {
        //Что происходит при принятии класс Swordsman
        player.sendMessage("Ку");
    }

    @Override
    public void onHit(Player player, LivingEntity entity) {
        //Что происходит при ударе игрока с классом Swordsman
        player.sendMessage("Тыдыщ");
    }

    @Override
    public void onDamaged(Player player, LivingEntity entity) {
        //Что происходит при ударе по игроку с классом Swordsman
        player.sendMessage("Ауч");
    }

    @Override
    public void onDeath(Player player) {
        //Что происходит когда игрока с классом Swordsman умирает
        player.sendMessage("Пока");
    }

    @Override
    public void onKill(Player player) {
        //Что происходит когда игрока с классом Swordsman убивает
        player.sendMessage("Лох");
    }

}
