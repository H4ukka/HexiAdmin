package h4;

import java.sql.*;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by H4 on 16.1.2015.
 */
public class DatabaseMethods {

    private JavaPlugin plugin;
    private String user;
    private String pass;
    private String url;

    DatabaseMethods (JavaPlugin plugin, PluginConfiguration config) {
        this.plugin = plugin;

        user = config.getString("mysql.user");
        pass = config.getString("mysql.pass");
        url = config.getString("mysql.url");
    }

    private Connection openConnection () {
        try {
            Connection newConnection = DriverManager.getConnection(url, user, pass);
            return newConnection;

        }catch(SQLException e1) {
            plugin.getLogger().warning("Failed to open MySQL connection.\n" + e1.getMessage());
        }

        return null;
    }

    private void executeUpdate (String query) {
        Connection conn = openConnection();

        if (conn != null) {
            try {
                PreparedStatement statement = conn.prepareStatement(query);
                statement.executeUpdate();
                statement.close();
                conn.close();
            }catch (SQLException e2){
                plugin.getLogger().warning("Failed to execute MySQL update.\n" + e2.getMessage());
            }
        }else{
            plugin.getLogger().warning("Failed to execute MySQL update. Connection was null.");
        }
    }

    private ResultSet executeQuery (String query) {
        Connection conn = openConnection();
        ResultSet answer = null;

        if (conn != null) {
            try {
                PreparedStatement statement = conn.prepareStatement(query);
                answer = statement.executeQuery();
                statement.close();
                conn.close();
            }catch (SQLException e2){
                plugin.getLogger().warning("Failed to execute MySQL query.\n" + e2.getMessage());
            }
        }else{
            plugin.getLogger().warning("Failed to execute MySQL query. Connection was null.");
        }

        return answer;
    }

}
