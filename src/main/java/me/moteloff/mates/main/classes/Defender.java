package me.moteloff.mates.main.classes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Defender implements RPGClass{
    @Override
    public void onChoice(Player player) {
        player.sendMessage("Вы выбрали Защитника"); // выбор класса
    }

    @Override
    public void onHit(Player player, LivingEntity entity) { // удар игрока с классом "Class"
        player.sendMessage("Трахать");
    }

    @Override
    public void onDamaged(Player player, LivingEntity entity) { // получение урона с класса "Class"
        player.sendMessage("ебальник снесло");
    }

    @Override
    public void onDeath(Player player) { // смерть "Class"
        player.sendMessage("Вы умерли");
    }

    @Override
    public void onKill(Player player) { // kill player with class "Class"
        player.sendMessage("Вы убили игрока");
    }

    @Override
    public void onBlockBreak(Player player) {
        player.sendMessage("Вы уничтожили блок");
    }
}
