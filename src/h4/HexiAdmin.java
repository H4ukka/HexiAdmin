package h4;

/**
 * HexiCraft Admin Tools
 * ver 0.1r0
 *
 * Created by h4 on 16.1.2015.
 *
 * Features:
 *  - None
 */

/**
 * TODO: Commands
 * TODO: MySQL Error Checking
 * TODO: User Messages
 * TODO: PEX support
 * TODO: Ban support
 */

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class HexiAdmin extends JavaPlugin implements Listener {

    private PluginConfiguration config = new PluginConfiguration(this);
    private DatabaseMethods dataBase = new DatabaseMethods(this, config);

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("HexiAdmin Started.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        switch (command.getName()) {
            case "warn":
                if (arguments.length < 1) {
                    return false;
                }else{
                    if (dataBase.warnPlayer(arguments[0]) > 0) {
                        sender.sendMessage("Warned " + arguments[0]);
                    }else{
                        sender.sendMessage("Could not find " + arguments[0]);
                    }
                }
        }
        return true;
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Map playerData = dataBase.getPlayerData(player.getUniqueId());

        if (playerData.size() == 0) {
            dataBase.addPlayer(player.getUniqueId(), player.getName());
        } else {
            if (!playerData.get("warnings").equals(0)) {
                player.sendMessage("You have warnings! :c");
            }
        }
    }
}
