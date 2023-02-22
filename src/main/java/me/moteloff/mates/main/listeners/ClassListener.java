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
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassListener implements Listener {

    DatabaseConstructor database = DatabaseConstructor.getInstance();

    @EventHandler
    public void onClassSwitch(PlayerSwitchClassEvent e) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class rpgClass = Class.forName("me.moteloff.mates.main.classes." + e.getPlayerClass());

        Method[] methods = rpgClass.getMethods();

        for (Method method : methods) {
            if (method.getName().equals("onChoice")) {
                Object swordsman = rpgClass.newInstance();
                method.invoke(swordsman, e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Entity entity = e.getEntity();
        Entity damager = e.getDamager();
        if (damager instanceof Player) {
            Player player = (Player) e.getDamager();
            String playerClass = database.getPlayerClass(player);
            if (playerClass != null) {
                Class rpgClass = Class.forName("me.moteloff.mates.main.classes." + playerClass);

                Method[] methods = rpgClass.getMethods();

                for (Method method : methods) {
                    if (method.getName().equals("onHit")) {
                        Object obj = rpgClass.newInstance();
                        method.invoke(obj, player, (LivingEntity) entity);
                    }
                }
            }
        } else if (entity instanceof Player) {
            Player player = (Player) e.getEntity();
            String playerClass = database.getPlayerClass(player);
            if (playerClass != null) {
                Class rpgClass = Class.forName("me.moteloff.mates.main.classes." + playerClass);

                Method[] methods = rpgClass.getMethods();

                for (Method method : methods) {
                    if (method.getName().equals("onDamaged")) {
                        Object obj = rpgClass.newInstance();
                        method.invoke(obj, player, (LivingEntity) damager);
                    }
                }
            }
        }
    }
}
