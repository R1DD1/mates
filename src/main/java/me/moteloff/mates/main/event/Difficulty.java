package me.moteloff.mates.main.event;

import me.moteloff.mates.main.Main;
import me.moteloff.mates.main.utils.Formatting;

public enum Difficulty {
    EASY(Formatting.translate(Main.getInstance().getEventCfg().getString("mob_arena.difficulty.easy"))),
    MEDIUM(Formatting.translate(Main.getInstance().getEventCfg().getString("mob_arena.difficulty.medium"))),
    HARD(Formatting.translate(Main.getInstance().getEventCfg().getString("mob_arena.difficulty.hard"))),
    IMPOSSIBLE(Formatting.translate(Main.getInstance().getEventCfg().getString("mob_arena.difficulty.impossible")));

    private String title;

    Difficulty(String title) {
        this.title = title;
    }

    public String getTitle(){
        return title;
    }
}
