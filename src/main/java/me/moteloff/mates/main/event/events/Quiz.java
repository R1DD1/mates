package me.moteloff.mates.main.event.events;

import me.moteloff.mates.main.Main;
import me.moteloff.mates.main.event.Difficulty;
import me.moteloff.mates.main.event.Event;
import me.moteloff.mates.main.event.Location;
import me.moteloff.mates.main.utils.Formatting;
import me.moteloff.mates.main.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Quiz implements Event {
    private final Main plugin = Main.getInstance();

    private Location location;
    private Boolean canJoin = false;
    private Boolean isActive = false;
    private List<Player> players;
    private List<Map<?, ?>> questions;
    private final Random random = new Random();
    private int questionCount;
    private Inventory choicerInv;
    private HashMap<Material, List<Player>> votes;
    private Material rightWool;
    private HashMap<Player, Integer> points;

    public Quiz(Location location) {
        this.location = location;
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
        questions = plugin.getEventCfg().getMapList("quiz.questions");
        questionCount = 0;
        choicerInv = Bukkit.createInventory(null, 36);
        votes = new HashMap<>();
        votes.put(Material.BLUE_WOOL, new ArrayList<>());
        votes.put(Material.RED_WOOL, new ArrayList<>());
        votes.put(Material.GREEN_WOOL, new ArrayList<>());
        votes.put(Material.YELLOW_WOOL, new ArrayList<>());

        players = new ArrayList<>();

        Main.eventItem = new ItemBuilder(Material.PAPER, 1)
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .displayName(plugin.getConfig().getString("quiz.item_title"))
                .lore(Formatting.translate(plugin.getConfig().getString("quiz.item_lore").replace("%loc%", getLocation().getTitle()).replace("%players%", String.valueOf(getPlayers().size()))).split("\n"))
                .build();

        setCanJoin(true);
        setActive(true);

        String[] preStartMsg = plugin.getConfig().getString("quiz.start_prepare").split("\n");
        for (String msg: preStartMsg) {
            Bukkit.broadcast(Component.text(ChatColor.translateAlternateColorCodes('&', msg)));}

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            String[] preStopMsg = plugin.getConfig().getString("quiz.stop_prepare").split("\n");
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
        points = new HashMap<>();
        for (Player player:getPlayers()) {points.put(player, 0);}
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                choicerInv.clear();
                Map<String, Object> question = (Map<String, Object>) getRandomQuestion();

                String questionText = getQuestionText(question);
                String rightAnswer = getRightAnswer(question);

                List<String> unrightAnswers = getUnrightAnswers(question);
                Collections.shuffle(unrightAnswers);

                List<String> allAnswers = new ArrayList<>(unrightAnswers);
                allAnswers.add(rightAnswer);

                prepareGUI(allAnswers, rightAnswer);
                showGUI();

                listener();

                questionCount++;
                updatePoints();
                if (questionCount >= 5) {
                    end();
                    timer.cancel();
                }
            }
        }, 0, 60 * 1000);
    }

    @Override
    public void end() {
        Player winner = getWinner();
        System.out.println("winner is " + winner.getName());
    }

    private Map<?, ?> getRandomQuestion() {
        int randomIndex = random.nextInt(questions.size());
        return questions.get(randomIndex);
    }

    private String getQuestionText(Map<String, Object> question) {
        return (String) question.get("question");
    }

    private String getRightAnswer(Map<String, Object> question) {
        return (String) question.get("right");
    }

    private List<String> getUnrightAnswers(Map<String, Object> question) {
        return (List<String>) question.get("unright");
    }

    private void prepareGUI(List<String> answers, String rightAnswer) {
        Material[] wools = new Material[] {Material.BLUE_WOOL, Material.RED_WOOL, Material.GREEN_WOOL, Material.YELLOW_WOOL};
        for (int i = 0; i<answers.size(); i++) {
            ItemStack item = new ItemBuilder(wools[i]).displayName(answers.get(i)).flags(ItemFlag.HIDE_ATTRIBUTES).build();
            if (answers.get(i).equals(rightAnswer)) {rightWool = wools[i];}
            choicerInv.addItem(item);
        }
    }

    private void showGUI() {
        for (Player player:players) {
            Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(choicerInv));
        }
    }

    private void listener() {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onInventoryClick(InventoryClickEvent event) {
                if (event.getInventory().equals(choicerInv)) {
                    ItemStack item = event.getCurrentItem();
                    if (item != null) {
                        List<Player> list = votes.get(item.getType());
                        list.add((Player) event.getWhoClicked());
                        votes.put(item.getType(), list);
                    }
                }
            }
        }, plugin);
    }

    private void updatePoints() {
        List<Player> players = votes.get(rightWool);
        for (Player player: players) {
            points.put(player, points.get(player) + 1);
        }
        System.out.println(players);
    }

    private Player getWinner() {
        Player playerWithMaxPoints = null;
        int maxPoints = Integer.MIN_VALUE;
        for (Map.Entry<Player, Integer> entry : points.entrySet()) {
            if (entry.getValue() > maxPoints) {
                maxPoints = entry.getValue();
                playerWithMaxPoints = entry.getKey();
            }
        }
        return playerWithMaxPoints;
    }
}
