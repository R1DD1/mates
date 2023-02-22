package me.moteloff.mates.main.listeners;

import me.moteloff.mates.main.Main;
import me.moteloff.mates.main.database.DatabaseConstructor;
import me.moteloff.mates.main.utils.Formatting;
import me.moteloff.mates.main.utils.entitybuilder.EntityBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Objects;

public class JoinListener implements Listener {

    DatabaseConstructor databaseConstructor = DatabaseConstructor.getInstance();
    Main plugin = Main.getInstance();

    public static final HashMap<Player, Integer> joiners = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (!databaseConstructor.exists(player.getName())) {
            player.kick(Component.text(Objects.requireNonNull(plugin.getConfig().getString("kick_messages.unknown_player"))));
        }

        player.sendMessage(Formatting.translate(Objects.requireNonNull(plugin.getConfig().getString("messages.join_msg"))));
        joiners.put(player, 3);

    }

    @EventHandler
    public void onLogin(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().startsWith("/login")) {
            Player player = e.getPlayer();
            String[] msg = e.getMessage().split(" ");
            if (joiners.containsKey(player)) {
                if (msg[1] != null) {
                    if (databaseConstructor.login(player.getName(), msg[1])) {
                        player.sendMessage(Formatting.translate(plugin.getConfig().getString("messages.join_suc")));
                        joiners.remove(player);
                    } else {
                        joiners.put(player, joiners.get(player) - 1);
                        if (joiners.get(player) == 0) {
                            player.kick(Component.text(Objects.requireNonNull(plugin.getConfig().getString("kick_messages.login_fail"))));
                        }
                        player.sendMessage(Formatting.translate(Objects.requireNonNull(Objects.requireNonNull(plugin.getConfig().getString("messages.join_fail"))
                                .replace("%tries%", String.valueOf(joiners.get(player))))));
                    }
                } else {
                    player.sendMessage(Formatting.translate(Objects.requireNonNull(plugin.getConfig().getString("messages.empty_pass"))));
                }
            }
        }
    }
}
