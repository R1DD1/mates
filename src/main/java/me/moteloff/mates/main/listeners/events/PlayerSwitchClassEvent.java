package me.moteloff.mates.main.listeners.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerSwitchClassEvent extends Event implements Cancellable {

    private Player player;
    private String playerClass;
    private boolean isCancelled;

    private static final HandlerList handlers = new HandlerList();

    public PlayerSwitchClassEvent(Player player, String playerClass) {
        this.player = player;
        this.playerClass = playerClass;
        this.isCancelled = false;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getPlayerClass() {
        return this.playerClass;
    }



    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean arg0) {
        this.isCancelled = arg0;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}