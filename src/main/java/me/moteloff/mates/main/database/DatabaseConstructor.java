package me.moteloff.mates.main.database;

import me.moteloff.mates.main.Main;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.Objects;

public class DatabaseConstructor {
    public static Connection conn;

    public static DatabaseConstructor INSTANCE = new DatabaseConstructor();

    public String CREATE_PLAYER = "INSERT INTO 'mates' (`NickName`,`password`,`vkID`,`seen`, `class`) VALUES  (?,?,?,?,?)";

    public void createTable() {
        try {
            Class.forName("org.sqlite.JDBC");
            if (conn != null) {conn.close();}

            conn = DriverManager.getConnection(Objects.requireNonNull(Main.getInstance().getConfig().getString("data_base_path")));
            String CREATE_TABLES = "CREATE TABLE IF NOT EXISTS 'mates' (`id` INTEGER, `NickName` VARCHAR(16) NULL DEFAULT NULL , `password` VARCHAR(32) NULL DEFAULT NULL , `vkID` INT NULL DEFAULT NULL ,`seen` VARCHAR(200) NULL DEFAULT NULL, `class` VARCHAR(16) NULL DEFAULT NULL, PRIMARY KEY (`id`));";
            PreparedStatement statement = conn.prepareStatement(CREATE_TABLES);
            statement.executeUpdate();
            conn.close();
            statement.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    public void createPlayer(String nick, String password,  int vkID) {
        try {
            conn = DriverManager.getConnection(Objects.requireNonNull(Main.getInstance().getConfig().getString("data_base_path")));
            PreparedStatement statement = conn.prepareStatement(this.CREATE_PLAYER);
            statement.setString(1, nick);
            statement.setString(2, password);
            statement.setInt(3, vkID);
            statement.setLong(4, 0L);
            statement.execute();
            conn.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Boolean exist(String username) {
        try {
            conn = DriverManager.getConnection(Objects.requireNonNull(Main.getInstance().getConfig().getString("data_base_path")));
            PreparedStatement statement = conn.prepareStatement("SELECT  * FROM 'mates' WHERE NickName=?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            conn.close();
            statement.close();
            resultSet.close();
        } catch (SQLException ignored) {}
        return false;
    }

    public Boolean login(String username, String password) {
        try {
            conn = DriverManager.getConnection(Objects.requireNonNull(Main.getInstance().getConfig().getString("data_base_path")));
            PreparedStatement statement = conn.prepareStatement("SELECT  * FROM 'mates' WHERE NickName=? and password=?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            conn.close();
            statement.close();
            resultSet.close();
        } catch (SQLException ignored) {}
        return false;
    }


    public String restorePass(String username, int vkID) {
        try {
            conn = DriverManager.getConnection(Objects.requireNonNull(Main.getInstance().getConfig().getString("data_base_path")));
            PreparedStatement statement = conn.prepareStatement("SELECT  * FROM 'mates' WHERE NickName=? and vkID=?");
            statement.setString(1, username);
            statement.setInt(2, vkID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println(resultSet.getString("password"));
                return resultSet.getString("password");
            }
            conn.close();
            statement.close();
            resultSet.close();
        } catch (SQLException ignored) {}
        return null;
    }

    public boolean updatePlayerClass(Player player, String playerClass) {
        try {
            conn = DriverManager.getConnection(Objects.requireNonNull(Main.getInstance().getConfig().getString("data_base_path")));
            PreparedStatement statement = conn.prepareStatement("UPDATE 'mates' SET `class`=? WHERE NickName=?");
            statement.setString(1, playerClass);
            statement.setString(2, player.getName());
            statement.executeUpdate();
            conn.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateTimeSeen(String player, long seen) {
        try {
            conn = DriverManager.getConnection(Objects.requireNonNull(Main.getInstance().getConfig().getString("data_base_path")));
            PreparedStatement statement = conn.prepareStatement("UPDATE 'mates' SET `seen`=? WHERE NickName=?");
            statement.setLong(1, seen);
            statement.setString(2, player);
            statement.executeUpdate();
            conn.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public long getTimeSeen(String player) {
        long dates = 0L;
        try{
            conn = DriverManager.getConnection(Objects.requireNonNull(Main.getInstance().getConfig().getString("data_base_path")));
            Connection connection = conn;
            PreparedStatement statement = connection.prepareStatement("SELECT  * FROM 'mates' WHERE NickName=?");
            statement.setString(1, player);
            statement.execute();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                dates = resultSet.getLong("seen");
            }
            conn.close();
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dates;
    }
}
