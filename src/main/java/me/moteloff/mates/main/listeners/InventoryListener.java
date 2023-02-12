package me.moteloff.mates.main.listeners;

import me.moteloff.mates.main.Main;
import me.moteloff.mates.main.event.Event;
import me.moteloff.mates.main.utils.Formatting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InventoryListener implements Listener {

    private final Main plugin = Main.getInstance();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack itemStack = e.getCurrentItem();

        if (e.getInventory().equals(Main.eventInv)) {
            if (itemStack != null && itemStack.equals(Main.eventItem)) {
                if (Main.activeEvent.getCanJoin()) {
                    Main.activeEvent.addPlayer(player);
                    player.teleport(Main.activeEvent.getLocation().getLocation());
                    Main.activeEvent.getLocation().getLocation().getBlock().setType(Material.DIRT);
                    player.closeInventory();
                } else {
                    player.sendMessage(Formatting.translate(plugin.getConfig().getString("mob_arena.cant_join")));
                }
            }
            e.setCancelled(true);
        }
    }
}
