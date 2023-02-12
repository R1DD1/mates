package me.moteloff.mates.main.commands;

import me.moteloff.mates.main.Main;
import me.moteloff.mates.main.utils.Formatting;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class Event implements CommandExecutor {

    private final Main plugin = Main.getInstance();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (Main.activeEvent.getActive()) {
                Main.eventInv.setItem(4, Main.eventItem);
                player.openInventory(Main.eventInv);
            } else {
                player.sendMessage(Formatting.translate(plugin.getConfig().getString("mob_arena.event_not_active")));
            }
        }
        return false;
    }
}
