package me.moteloff.mates.main.event.events;

import me.moteloff.mates.main.Main;
import me.moteloff.mates.main.event.Difficulty;
import me.moteloff.mates.main.event.Event;
import me.moteloff.mates.main.event.Location;
import me.moteloff.mates.main.utils.Formatting;
import me.moteloff.mates.main.utils.ItemBuilder;
import me.moteloff.mates.main.utils.LabelsHelper;
import me.moteloff.mates.main.utils.entitybuilder.EntityBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

public class MobArena implements Event {

    private final Main plugin = Main.getInstance();
    private Difficulty difficulty;
    private Location location;
    private Boolean canJoin = false;
    private Boolean isActive = false;
    private List<Player> players = new ArrayList<>();

    public MobArena(Difficulty difficulty, Location location) {
        this.difficulty = difficulty;
        this.location = location;
    }
    @Override
    public Difficulty getDifficulty() {
        return difficulty;
    }
    @Override
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
    @Override
    public Location getLocation() {
        return location;
    }
    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
    @Override
    public Boolean getCanJoin() {
        return canJoin;
    }
    @Override
    public void setCanJoin(Boolean canJoin) {
        this.canJoin = canJoin;
    }
    @Override
    public Boolean getActive() {
        return isActive;
    }
    @Override
    public void setActive(Boolean active) {
        isActive = active;
    }
    @Override
    public List<Player> getPlayers() {
        return players;
    }
    @Override
    public void addPlayer(Player player) {
        this.players.add(player);
    }
    @Override
    public void register() {
        Main.eventItem = new ItemBuilder(Material.DIAMOND_SWORD, 1)
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .displayName(plugin.getConfig().getString("mob_arena.item_title"))
                .lore(Formatting.translate(plugin.getConfig().getString("mob_arena.item_lore").replace("%dif%", getDifficulty().getTitle()).replace("%loc%", getLocation().getTitle()).replace("%players%", String.valueOf(getPlayers().size()))).split("\n"))
                .build();

        setCanJoin(true);
        setActive(true);

        String[] preStartMsg = plugin.getConfig().getString("mob_arena.start_prepare").split("\n");
        for (String msg: preStartMsg) {Bukkit.broadcast(Component.text(ChatColor.translateAlternateColorCodes('&', msg.replace("%loc%", getLocation().getTitle()).replace("%dif%", getDifficulty().getTitle()))));}

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            String[] preStopMsg = plugin.getConfig().getString("mob_arena.stop_prepare").split("\n");
            start();
            for (String msg: preStopMsg) {Bukkit.broadcast(Component.text(ChatColor.translateAlternateColorCodes('&', msg)));}
            setCanJoin(false);
        }, 20*6*2);
    }

    @Override
    public void unregister() {
        Main.eventInv.clear(4);
        setActive(false);
    }

    @Override
    public void start() {
        int[] yValues = new int[] {69, 70, 71};
        LabelsHelper.getLabels(getLocation().getLocation(), 30, yValues ,"mob", (sign) -> {
            EntityBuilder.testZombie().onHit(event -> {
                event.getEntity().setCustomName("Ouch!");

            }).onDamaged(event -> {
                event.getEntity().setCustomName("I'm hurt!");

            }).spawnEntity(sign.getLocation());

        });
    }
}
