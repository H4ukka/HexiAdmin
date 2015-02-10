package com.hexicraft.h4ukka;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Created by h4 on 10.2.2015.
 *
 * This command allows the user to check their own HexiAdmin status or someone else's status
 * if they have permissions to do so.
 *
 * PERMNODES:
 * hexiadmin.player
 * hexiadmin.mod
 */

public class StatusCheckCommand implements CommandExecutor {

    private final HexiAdmin     plugin;

    private PluginConfiguration config;
    private DatabaseMethods     dataBase;

    public StatusCheckCommand (HexiAdmin plugin) {
        this.plugin     = plugin;
        this.config     = plugin.getConfiguration();
        this.dataBase   = plugin.getDataBase();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        /* STATUS CHECK COMMAND */

        if (commandSender instanceof Player) {
            /* PLAYER */

            Player target = (Player) commandSender;

            if (plugin.hasPermission(target, "hexiadmin.player")) {
                /* PLAYER AS MEMBER */

                if (strings.length > 0) {
                    commandSender.sendMessage(config.getString("Messages.Warnings.PermStatusOthers"));
                    return false;
                }

                Map playerInfo = dataBase.getPlayerData(target.getUniqueId());

                target.sendMessage(String.format(config.getString(
                        "Messages.Status.OwnStatus"),
                        playerInfo.get("warnings").toString()));

            }else if(plugin.hasPermission(target, "hexiadmin.mod") ||
                     plugin.hasPermission(target, "hexiadmin.admin")) {
                /* PLAYER AS MOD OR ADMIN */

                /**
                 * ARGUMENTS: [NAME] [GARBAGE]
                 * Doesn't matter if we receive more than a single argument;
                 * the extra arguments will be ignored.
                 * We'll notify the user about them tho.
                 */

                Map playerInfo = dataBase.getPlayerData(strings[0]);

                if (strings.length >= 1) {

                    if (strings.length > 1) {
                        commandSender.sendMessage("Messages.Info.GarbageIgnore");
                    }

                    target.sendMessage(String.format(config.getString(
                            "Messages.Status.StatusOthers"),
                            playerInfo.get("warnings").toString()));
                }
            }

        }else {
            /* CONSOLE */

            if (strings.length > 0) {
                /**
                 * ARGUMENTS: [NAME] [GARBAGE]
                 * Doesn't matter if we receive more than a single argument;
                 * the extra arguments will be ignored.
                 * We'll notify the user about them tho.
                 */

                if (strings.length != 1) {
                    commandSender.sendMessage("Ignoring garbage arguments...");
                }

                Map playerInfo = dataBase.getPlayerData(strings[0]);

                commandSender.sendMessage(String.format(config.getString(
                        "Messages.Status.StatusOthers"),
                        playerInfo.get("warnings").toString()));

            }else {

                commandSender.sendMessage(config.getString("Messages.Warnings.ConsoleOwnStatus"));
            }
        }
        return false;
    }
}
