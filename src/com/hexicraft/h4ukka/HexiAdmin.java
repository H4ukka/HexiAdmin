package com.hexicraft.h4ukka;

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

import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class HexiAdmin extends JavaPlugin implements Listener {

    private PluginConfiguration config = new PluginConfiguration(this);
    private DatabaseMethods dataBase = new DatabaseMethods(this, config);

    public static Permission permissions = null;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("HexiAdmin Started.");

        setupPermissions();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
      /*switch (command.getName()) {
            case "warn":
                if (arguments.length < 1) {
                    return false;
                }else if (permissions.has(sender, "hexiadmin.mod")) {
                    if (dataBase.warnPlayer(arguments[0]) > 0) {
                        sender.sendMessage("Warned " + arguments[0]);
                    }else{
                        sender.sendMessage("Could not find " + arguments[0]);
                    }
                }
        }*/
        if (arguments.length < 1) {
            /* HELP */
        }else if (command.getName().equalsIgnoreCase("warn")) {
            if (sender instanceof Player) {
                Player target = (Player) sender;

                /** Usage /warn <command> <argument>
                 *
                 *  Permission nodes:
                 *
                 *  hexiadmin.player
                 *  hexiadmin.mod
                 *  hexiadmin.admin
                 */

                switch (arguments[0]) {

                    /* Lists player's own warning status */
                    case "mystatus":
                        if (permissions.has(sender, "hexiadmin.player")) {
                            Map playerInfo = dataBase.getPlayerData(target.getUniqueId());
                            target.sendMessage(playerInfo.get("warnings").toString());
                        }
                        break;

                    /* Warns the given player with an optional reason*/
                    case "w":
                        if (permissions.has(sender, "hexicraft.mod")) {

                        }
                        break;

                    /* Lists the status of a player */
                    case "status":
                        if (permissions.has(sender, "hexicraft.mod")) {

                        }
                        break;

                    /* Resets a players status */
                    case "reset":
                        if (permissions.has(sender, "hexicraft.admin")) {

                        }
                        break;

                    default:
                        sender.sendMessage("Command not found.");
                }
            }else{
                /* NOT A PLAYER */
            }
        }
        return true;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permissions = rsp.getProvider();
        return permissions != null;
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
