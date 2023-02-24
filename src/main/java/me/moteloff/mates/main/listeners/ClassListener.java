package me.moteloff.mates.main.listeners;

import me.moteloff.mates.main.classes.RPGClass;
import me.moteloff.mates.main.classes.Swordsman;
import me.moteloff.mates.main.database.DatabaseConstructor;
import me.moteloff.mates.main.listeners.events.PlayerSwitchClassEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassListener implements Listener {

    DatabaseConstructor database = DatabaseConstructor.getInstance();

    @EventHandler
    public void onClassSwitch(PlayerSwitchClassEvent e) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        callMethod("me.moteloff.mates.main.classes." + e.getPlayerClass(), "onChoice", e.getPlayer());
    }

    @EventHandler
    public void onGetDamage(EntityDamageByEntityEvent e) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Entity entity = e.getEntity();
        Entity damager = e.getDamager();
        if (damager instanceof Player) {
            Player player = (Player) e.getDamager();
            String playerClass = database.getPlayerClass(player);
            if (playerClass != null) {
                LivingEntity entity1 = (LivingEntity) entity;
                callMethod("me.moteloff.mates.main.classes." + playerClass, "onHit", player, entity);
                if (entity1.getHealth() <= e.getDamage()) {
                    callMethod("me.moteloff.mates.main.classes." + playerClass, "onKill", player);
                }
            }
        } else if (entity instanceof Player) {
            Player player = (Player) e.getEntity();
            String playerClass = database.getPlayerClass(player);
            if (playerClass != null) {
                callMethod("me.moteloff.mates.main.classes." + playerClass, "onDamaged", player, damager);
                if (player.getHealth() <= e.getDamage()) {
                    callMethod("me.moteloff.mates.main.classes." + playerClass, "onDeath", player);
                }
            }
        }
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Player player = e.getPlayer();
        String playerClass = database.getPlayerClass(player);
        if(playerClass != null) {
            callMethod("me.moteloff.mates.main.classes." + playerClass, "onBlockBreak", player);
        } // хз правильно или нет если чо исправь
    }

    private void callMethod(String className, String methodName, Player player, Entity entity) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class rpgClass = Class.forName(className);

        Method[] methods = rpgClass.getMethods();

        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Object obj = rpgClass.newInstance();
                method.invoke(obj, player, (LivingEntity) entity);
            }
        }
    }

    private void callMethod(String className, String methodName, Player player) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class rpgClass = Class.forName(className);

        Method[] methods = rpgClass.getMethods();

        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Object obj = rpgClass.newInstance();
                method.invoke(obj, player);
            }
        }
    }
}
