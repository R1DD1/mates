package me.moteloff.mates.main.listeners;

import com.ubivashka.vk.bukkit.BukkitVkApiPlugin;
import com.ubivashka.vk.bukkit.events.VKMessageEvent;
import com.ubivashka.vk.bukkit.events.VKUserGroupJoinEvent;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import me.moteloff.mates.main.Main;
import me.moteloff.mates.main.database.DatabaseConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Random;

public class VkListener implements Listener {

    private static final VkApiClient CLIENT = (BukkitVkApiPlugin.getPlugin(BukkitVkApiPlugin.class)).getVkApiProvider().getVkApiClient();
    private static final GroupActor ACTOR = (BukkitVkApiPlugin.getPlugin(BukkitVkApiPlugin.class)).getVkApiProvider().getActor();
    private static final Random RANDOM = new Random();

    private final DatabaseConstructor databaseConstructor = DatabaseConstructor.getInstance();
    private final Main plugin = Main.getInstance();

    @EventHandler
    public void onJoin(VKUserGroupJoinEvent e) throws ClientException, ApiException {
        CLIENT.messages().send(ACTOR).randomId(RANDOM.nextInt()).peerId(e.getJoin().getUserId()).message("welcome").execute();
    }

    @EventHandler
    public void onRegister(VKMessageEvent e) throws ClientException, ApiException {
        if (e.getMessage().getText().startsWith(".register")) {
            String[] args = e.getMessage().getText().split(" ");
            String username = args[1];
            String pass = args[2];
            if (databaseConstructor.exists(username)) {
                CLIENT.messages().send(ACTOR).randomId(RANDOM.nextInt()).peerId(e.getPeer()).message(
                        plugin.getConfig().getString("vk_messages.already_exist")
                ).execute();
            } else {
                CLIENT.messages().send(ACTOR).randomId(RANDOM.nextInt()).peerId(e.getPeer()).message(
                        plugin.getConfig().getString("vk_messages.register_up")
                ).execute();
                databaseConstructor.createPlayer(username, pass, e.getUserId());
            }
        }
    }

    @EventHandler
    public void onRestorePass(VKMessageEvent e) throws ClientException, ApiException {
        if (e.getMessage().getText().startsWith(".restorepass")) {
            String[] args = e.getMessage().getText().split(" ");
            String username = args[1];
            if (databaseConstructor.exists(username)) {
                String password = databaseConstructor.restorePass(username, e.getUserId());
                if (password != null) {
                    CLIENT.messages().send(ACTOR).randomId(RANDOM.nextInt()).peerId(e.getPeer()).message(
                            plugin.getConfig().getString("vk_messages.restore_pass").replace("%pass%", password)).execute();
                }
            }
        }
    }
}
