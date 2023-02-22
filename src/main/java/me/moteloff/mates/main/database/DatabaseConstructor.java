package me.moteloff.mates.main.database;

import me.moteloff.mates.main.Main;
import me.moteloff.mates.main.listeners.events.PlayerSwitchClassEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.Objects;

public class DatabaseConstructor {
    private static final String CREATE_PLAYER = "INSERT INTO 'mates' (`NickName`,`password`,`vkID`,`seen`, `class`) VALUES  (?,?,?,?,?)";


    private static Connection connection;

    private static final DatabaseConstructor INSTANCE = new DatabaseConstructor();

    public static DatabaseConstructor getInstance() {
        return INSTANCE;
    }

    private DatabaseConstructor() {}

    public void createTable() {
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(Main.getInstance().getConfig().getString("data_base_path"));
                 PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS 'mates' (`id` INTEGER, `NickName` VARCHAR(16) NULL DEFAULT NULL , `password` VARCHAR(32) NULL DEFAULT NULL , `vkID` INT NULL DEFAULT NULL ,`seen` VARCHAR(200) NULL DEFAULT NULL, `class` VARCHAR(16) NULL DEFAULT NULL, PRIMARY KEY (`id`))")) {
                statement.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlayer(String nick, String password, int vkID) {
        try (Connection connection = DriverManager.getConnection(Main.getInstance().getConfig().getString("data_base_path"));
             PreparedStatement statement = connection.prepareStatement(CREATE_PLAYER)) {
            statement.setString(1, nick);
            statement.setString(2, password);
            statement.setInt(3, vkID);
            statement.setLong(4, 0L);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Boolean exists(String username) {
        try (Connection conn = DriverManager.getConnection(Objects.requireNonNull(Main.getInstance().getConfig().getString("data_base_path")));
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM 'mates' WHERE NickName=?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean login(String username, String password) {
        try (Connection conn = DriverManager.getConnection(Objects.requireNonNull(Main.getInstance().getConfig().getString("data_base_path")));
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM 'mates' WHERE NickName=? AND password=?")) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String restorePass(String username, int vkID) {
        try (Connection conn = DriverManager.getConnection(Objects.requireNonNull(Main.getInstance().getConfig().getString("data_base_path")));
             PreparedStatement statement = conn.prepareStatement("SELECT password FROM mates WHERE NickName=? and vkID=?")) {
            statement.setString(1, username);
            statement.setInt(2, vkID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println(resultSet.getString("password"));
                    return resultSet.getString("password");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updatePlayerClass(Player player, String playerClass) {
        try (Connection conn = DriverManager.getConnection(Objects.requireNonNull(Main.getInstance().getConfig().getString("data_base_path")));
             PreparedStatement statement = conn.prepareStatement("UPDATE 'mates' SET `class`=? WHERE NickName=?")) {
            statement.setString(1, playerClass);
            statement.setString(2, player.getName());
            statement.executeUpdate();

            PlayerSwitchClassEvent event = new PlayerSwitchClassEvent(player, playerClass);
            Bukkit.getPluginManager().callEvent(event);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String getPlayerClass(Player player) {
        try (Connection conn = DriverManager.getConnection(Objects.requireNonNull(Main.getInstance().getConfig().getString("data_base_path")));
             PreparedStatement statement = conn.prepareStatement("SELECT class FROM 'mates' WHERE NickName=?")) {
            statement.setString(1, player.getName());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("class");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateTimeSeen(String player, long seen) {
        try (Connection conn = DriverManager.getConnection(Objects.requireNonNull(Main.getInstance().getConfig().getString("data_base_path")));
             PreparedStatement statement = conn.prepareStatement("UPDATE 'mates' SET `seen`=? WHERE NickName=?")) {
            statement.setLong(1, seen);
            statement.setString(2, player);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public long getTimeSeen(String player) {
        long dates = 0L;
        try (Connection conn = DriverManager.getConnection(Objects.requireNonNull(Main.getInstance().getConfig().getString("data_base_path")));
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM 'mates' WHERE NickName=?")) {
            statement.setString(1, player);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    dates = resultSet.getLong("seen");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dates;
    }
}
