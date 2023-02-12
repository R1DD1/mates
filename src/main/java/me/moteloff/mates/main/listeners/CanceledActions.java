package me.moteloff.mates.main.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class CanceledActions implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (JoinListener.joiners.containsKey(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (JoinListener.joiners.containsKey(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if (JoinListener.joiners.containsKey((Player) e.getDamager()) || JoinListener.joiners.containsKey((Player) e.getEntity())) {
            e.setCancelled(true);
        }
    }
}
