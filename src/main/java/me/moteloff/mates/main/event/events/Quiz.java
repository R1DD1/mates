package me.moteloff.mates.main.event.events;

import me.moteloff.mates.main.Main;
import me.moteloff.mates.main.event.Event;
import me.moteloff.mates.main.utils.Formatting;
import me.moteloff.mates.main.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Quiz implements Event {
    private final Main plugin = Main.getInstance();
    private Boolean canJoin = false;
    private Boolean isActive = false;
    private List<Player> players;
    private List<Map<?, ?>> questions;
    private final Random random = new Random();
    private int questionCount;
    private static Inventory choicerInv;
    private HashMap<Integer, Player> votes;

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
        players = new ArrayList<>();

        Main.eventItem = new ItemBuilder(Material.PAPER, 1)
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .displayName(plugin.getConfig().getString("quiz.item_title"))
                .lore(Formatting.translate(plugin.getConfig().getString("quiz.item_lore").replace("%players%", String.valueOf(getPlayers().size()))).split("\n"))
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
    public static void test(Player player) {
        player.openInventory(choicerInv);
    }

    @Override
    public void start() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Map<String, Object> question = (Map<String, Object>) getRandomQuestion();
                // Получаем текст вопроса и правильный ответ
                String questionText = getQuestionText(question);
                String rightAnswer = getRightAnswer(question);
                // Получаем список неправильных ответов и перемешиваем его
                List<String> unrightAnswers = getUnrightAnswers(question);
                Collections.shuffle(unrightAnswers);
                // Составляем список всех ответов
                List<String> allAnswers = new ArrayList<>(unrightAnswers);
                allAnswers.add(rightAnswer);
                prepareGUI(allAnswers);

//                // Составляем текст сообщения с вопросом и ответами
//                StringBuilder messageBuilder = new StringBuilder();
//                messageBuilder.append(questionText).append("\n");
//                for (int i = 0; i < allAnswers.size(); i++) {
//                    messageBuilder.append(i + 1).append(". ").append(allAnswers.get(i)).append("\n");
//                }
//                String message = messageBuilder.toString();
//                // Отправляем сообщение в чат
//                plugin.getServer().broadcastMessage(message);
//                // Увеличиваем счетчик заданных вопросов
                questionCount++;
                // Если задано 5 вопросов, останавливаем таймер
                if (questionCount >= 5) {
                    timer.cancel();
                }
            }
        }, 0, 60 * 1000);
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

    private void prepareGUI(List<String> answers) {
        Material[] wools = new Material[] {Material.BLUE_WOOL, Material.RED_WOOL, Material.GREEN_WOOL, Material.YELLOW_WOOL};
        for (int i = 0; i<answers.size(); i++) {
            ItemStack item = new ItemBuilder(wools[i]).displayName(answers.get(i)).flags(ItemFlag.HIDE_ATTRIBUTES).build();
            choicerInv.addItem(item);
        }
    }
}
