package com.hexicraft.h4ukka;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

public class DatabaseMethods {

    private JavaPlugin plugin;
    private String user;
    private String pass;
    private String url;
    private String tableName;

    DatabaseMethods (JavaPlugin plugin, PluginConfiguration config) {
        this.plugin = plugin;

        user = config.getString("mysql.user");
        pass = config.getString("mysql.pass");
        url = config.getString("mysql.url");
        tableName = config.getString("mysql.tableName");
    }

    private Connection openConnection () {
        try {
            return DriverManager.getConnection(url, user, pass);

        }catch(SQLException e1) {
            plugin.getLogger().warning("Failed to open MySQL connection.\n" + e1.getMessage());
        }

        return null;
    }

    private int executeUpdate (String query) {
        Connection conn = openConnection();
        PreparedStatement queryStatement = null;

        if (conn != null) {
            try {
                queryStatement = conn.prepareStatement(query);
                return queryStatement.executeUpdate();
            }catch (SQLException e2){
                plugin.getLogger().warning("Failed to execute MySQL update.\n" + e2.getMessage());
            }finally{
                try {
                    if (queryStatement != null) queryStatement.close();
                    if (conn != null) conn.close();
                }catch(SQLException e4) {
                    plugin.getLogger().warning("Error while closing MySQL query.\n" + e4.getMessage());
                }
            }
        }else{
            plugin.getLogger().warning("Failed to execute MySQL update. Connection was null.");
        }
        return -1;
    }

    private Map executeQuery (String query) {
        Connection conn = openConnection();
        ResultSet answer = null;
        PreparedStatement queryStatement = null;
        Map player = new HashMap();

        if (conn != null) {
            try {
                queryStatement = conn.prepareStatement(query);
                answer = queryStatement.executeQuery();

                if(answer.next()) {
                    player.put("uuid", answer.getString("uuid"));
                    player.put("name", answer.getString("name"));
                    player.put("warnings", answer.getInt("warnings"));
                }
            }catch (SQLException e3){
                plugin.getLogger().warning("Failed to execute MySQL query.\n" + e3.getMessage());
            }finally{
                try {
                    if (answer != null) answer.close();
                    if (queryStatement != null) queryStatement.close();
                    if (conn != null) conn.close();
                }catch(SQLException e4) {
                    plugin.getLogger().warning("Error while closing MySQL query.\n" + e4.getMessage());
                }
            }
        }else{
            plugin.getLogger().warning("Failed to execute MySQL query. Connection was null.");
        }
        return player;
    }

    public int setupDataBase () {
        return executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                             "uuid VARCHAR(36) NOT NULL," +
                             "name VARCHAR(16) NOT NULL," +
                             "warnings TINYINT DEFAULT 0," +
                             "reasons VARCHAR(80) DEFAULT NULL," +
                             "issuers VARCHAR(80) DEFAULT NULL," +
                             "previous_names BLOB ," +
                             "reset_date DATETIME DEFAULT NULL," +
                             "PRIMARY KEY (uuid)" +
                             ")");
    }

    public Map getPlayerData (UUID playerUniqueId) {
        return executeQuery("SELECT * FROM " + tableName + " WHERE uuid = '" + playerUniqueId.toString() + "'");
    }

    public Map getPlayerData (String playerName) {
        return executeQuery("SELECT * FROM " + tableName + " WHERE name = '" + playerName + "'");
    }

    public int updatePlayerName (UUID playerUniqueId, String playerName) {
        return executeUpdate("UPDATE " + tableName + " SET previous_names = CONCAT(previous_names, name, ','), " +
                             "name = '" + playerName + "' WHERE uuid = '" + playerUniqueId.toString() + "'");
    }

    public int addPlayer (UUID playerUniqueId, String playerName) {
        return executeUpdate("INSERT INTO " + tableName + " (name,uuid,previous_names) VALUES('" + playerName + "','" + playerUniqueId.toString() + "','')");
    }

    public int warnPlayer (String playerName) {
        return executeUpdate("UPDATE " + tableName + " SET warnings = warnings + 1 WHERE name = '" + playerName + "'");
    }

}
