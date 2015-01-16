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
 * TODO: Write the plugin
 * TODO: Flatfile or MySQL?
 */

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class HexiAdmin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("HexiAdmin Started.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        sender.sendMessage("Hello");
        return false;
    }
}
