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

                if (arguments.length > 0) {

                    switch (arguments[0]) {

                        /* Lists player's own warning status */
                        case "mystatus":
                            if (permissions.has(sender, "hexiadmin.player")) {
                                Map playerInfo = dataBase.getPlayerData(target.getUniqueId());
                                target.sendMessage(playerInfo.get("warnings").toString());
                            }
                            break;

                        /* Warns the given player with an optional reason */
                        /* Usage: /warn w <target> */
                        case "w":
                            if (permissions.has(sender, "hexicraft.mod")) {
                                if (arguments.length > 1) {
                                    if (dataBase.warnPlayer(arguments[1]) > 0){
                                        sender.sendMessage("Warned " + arguments[1]);
                                    }else{
                                        sender.sendMessage("Could not find " + arguments[1]);
                                    }
                                }else{
                                    /* NOT ENOUGH ARGUMENTS */
                                }
                            }
                            break;

                        /* Lists the status of a player */
                        /* Usage: /warn status <target> */
                        case "status":
                            if (permissions.has(sender, "hexicraft.mod")) {
                                if (arguments.length > 1) {
                                    Map targetPlayerData = dataBase.getPlayerData(arguments[1]);

                                    if (!(targetPlayerData.size() == 0)) {
                                        sender.sendMessage(arguments[1] + " has " + targetPlayerData.get("warnings").toString() + " warnings");
                                    }else{
                                        sender.sendMessage("Could not find " + arguments[1]);
                                    }
                                }else{
                                    /* NOT ENOUGH ARGUMENTS */
                                }
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
                    /* NOT ENOUGH ARGUMENTS */
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
            int warningCount = (Integer) playerData.get("warnings");

            if (warningCount > 0) {
                player.sendMessage("You have warnings :c");
            }else if (warningCount >= 3){
                /* BAN PLAYER */
            }
        }
    }
}