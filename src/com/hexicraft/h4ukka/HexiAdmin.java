package com.hexicraft.h4ukka;

/**
 * HexiCraft Admin Tools
 * ver 0.1r0
 *
 * Created by h4 on 16.1.2015.
 *
 * Remember to build for java version 1.7
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

        dataBase.setupDataBase();

        setupPermissions();

        getCommand("status").setExecutor(new StatusCheckCommand(this));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {

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

                        /* Warns the given player with an optional reason */
                        /* Usage: /warn w <target> */
                        case "w":
                            if (permissions.has(sender, "hexiadmin.mod")) {
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

                        /* Resets a players status */
                        case "reset":
                            if (permissions.has(sender, "hexiadmin.admin")) {

                            }
                            break;

                        default:
                            sender.sendMessage("Command not found.");
                    }
                }else{
                    /* NOT ENOUGH ARGUMENTS */
                    return false;
                }
            }else{
                /* NOT A PLAYER */
                return false;
            }
        }
        return true;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permissions = rsp.getProvider();
        return permissions != null;
    }

    public Permission getPermissions () {
        return permissions;
    }

    public boolean hasPermission (CommandSender cs, String node) {
        return permissions.has(cs, node);
    }

    public boolean hasPermission (Player pl, String node) {
        return permissions.has(pl, node);
    }

    public PluginConfiguration getConfiguration () {
        return config;
    }

    public DatabaseMethods getDataBase () {
        return dataBase;
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Map playerData = dataBase.getPlayerData(player.getUniqueId());

        if (playerData.size() == 0) {
            dataBase.addPlayer(player.getUniqueId(), player.getName());
        } else {
            int warningCount = (Integer) playerData.get("warnings");
            String currentPlayerName = player.getName();
            String storedPlayerName = (String) playerData.get("name");

            if (warningCount > 0) {
                player.sendMessage("You have warnings :c");
            }else if (warningCount >= 3){
                /* BAN PLAYER */
            }

            if (!storedPlayerName.equals(currentPlayerName)) {
                /* Stored name doesn't match the current name */

                getLogger().info("Updating player record for " + currentPlayerName + " - old name was " + storedPlayerName);
                dataBase.updatePlayerName(player.getUniqueId(), currentPlayerName);
            }
        }
    }
}
