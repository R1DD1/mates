package me.moteloff.mates.main.listeners;

import me.moteloff.mates.main.Main;
import me.moteloff.mates.main.database.DatabaseConstructor;
import me.moteloff.mates.main.event.Event;
import me.moteloff.mates.main.utils.Formatting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class InventoryListener implements Listener {

    private final Main plugin = Main.getInstance();
    private final DatabaseConstructor database = DatabaseConstructor.getInstance();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack itemStack = e.getCurrentItem();

        if (e.getInventory().equals(Main.eventInv)) {
            if (itemStack != null && itemStack.equals(Main.eventItem)) {
                if (Main.activeEvent.getCanJoin()) {
                    Main.activeEvent.addPlayer(player);
                    player.teleport(Main.activeEvent.getLocation().getLocation());
                    if (Main.activeEvent.getPlayers().contains(player)) {Main.activeEvent.addPlayer(player);}
                    player.closeInventory();
                } else {
                    player.sendMessage(Formatting.translate(plugin.getConfig().getString("event.cant_join")));
                }
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void classChoicer(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item != null) {
            if (e.getInventory().equals(Main.classInv)) {
                Player player = (Player) e.getWhoClicked();
                PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
                if (container.has(new NamespacedKey(plugin, "rpg_class"), PersistentDataType.STRING)) {
                    String rpgClass = container.get(new NamespacedKey(plugin, "rpg_class"), PersistentDataType.STRING);
                    database.updatePlayerClass(player, rpgClass);
                }
            }
        }
    }
}
