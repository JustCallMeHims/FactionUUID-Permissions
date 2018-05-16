package com.oneshotmc.factionperms.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

public class ChatUtil {

    public static void sendMessage(CommandSender sender, String... messages){
        for (String message : messages) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public static String readableMaterial(Material material){
        return material.name().toLowerCase().replaceAll("_"," ");
    }

}
