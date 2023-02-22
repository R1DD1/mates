package me.moteloff.mates.main.utils.entitybuilder;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class CustomEntityListener implements Listener {

    private final Consumer<EntityDamageByEntityEvent> onHit;
    private final Consumer<EntityDamageEvent> onDamaged;


    public CustomEntityListener(Consumer<EntityDamageByEntityEvent> onHit, @Nullable Consumer<EntityDamageEvent> onDamaged) {
        this.onHit = onHit;
        this.onDamaged = onDamaged;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && onHit != null) {
            onHit.accept(event);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (onDamaged != null) {
            onDamaged.accept(event);
        }
    }
}