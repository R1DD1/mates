package me.moteloff.mates.main.event;

import org.bukkit.entity.Player;

import java.util.List;

public interface Event {

    default Difficulty getDifficulty() {return null;}

    default void setDifficulty(Difficulty difficulty) {
    }

    default Location getLocation() {return null;}

    default void setLocation(Location location) {}

    default Boolean getCanJoin() {return false;}

    default void setCanJoin(Boolean canJoin) {}

    default Boolean getActive() { return false; }

    default void setActive(Boolean active) {}

    default List<Player> getPlayers() {return null;}
    default void addPlayer(Player player) {}
    default void register() {}
    default void unregister() {}

    default void start() {}

    default void end() {}
}


