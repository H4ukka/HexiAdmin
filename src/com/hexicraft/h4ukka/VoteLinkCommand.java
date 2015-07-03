package com.hexicraft.h4ukka;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import java.util.List;
import java.util.Map;

/**
 * Created by H4 on 9.6.2015.
 */
public class VoteLinkCommand implements CommandExecutor {

    private final HexiAdmin plugin;

    private PluginConfiguration config;

    public VoteLinkCommand (HexiAdmin plugin) {
        this.plugin     = plugin;
        this.config     = plugin.getConfiguration();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        List<String> voteUrl = config.getStringList("voteurls");
        List<String> voteText = config.getStringList("votetexts");
        List<String> hoverText = config.getStringList("hovertexts");

        String title = config.getString("votetitle");
        String bottom = config.getString("bottom");


        if (commandSender instanceof Player) {

            Player target = (Player) commandSender;

            // Title
            target.spigot().sendMessage(new ComponentBuilder("").create());
            target.spigot().sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', title)).create());
            target.spigot().sendMessage(new ComponentBuilder("").create());

            for (int i = 0; i < voteUrl.size(); i++) {

                TextComponent msg = new TextComponent( ChatColor.translateAlternateColorCodes('&', voteText.get(i)) );
                msg.setClickEvent( new ClickEvent(ClickEvent.Action.OPEN_URL, voteUrl.get(i)));
                msg.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( hoverText.get(i) ).create()));
                target.spigot().sendMessage(msg);
            }

            // Bottom
            target.spigot().sendMessage(new ComponentBuilder("").create());
            target.spigot().sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', bottom)).create());
            target.spigot().sendMessage(new ComponentBuilder("").create());

        }else {

        }

        return true;
    }
}