package com.oneshotmc.factionperms.configuration;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class Configuration {

    private static YamlConfiguration config;

    private String messagesPlaceDeny;
    private String messagesInteractDeny;
    private String messagesBreakDeny;
    private String messagesFPermsUsage;

    public Configuration(File file){
        config = new YamlConfiguration();
        try {
            config.load(file);
            init();
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void init(){
        messagesPlaceDeny = ChatColor.translateAlternateColorCodes('&',getOrSet("messages.place-deny",
                "&e&l(!) &eYou cannot place %material% here because of your current groups. If you think this is a mistake" +
                        ", consult your faction leader."));
        messagesInteractDeny = ChatColor.translateAlternateColorCodes('&',getOrSet("messages.interact-deny","&e&l(!) &eYou cannot interact with %material% here because of your current groups" +
                ". If you think this is a mistake, consult your faction leader."));
        messagesBreakDeny = ChatColor.translateAlternateColorCodes('&', getOrSet("messages.break-deny","&e&l(!) &eYou cannot break %material% here because of your current groups" +
                ". If you think this is a mistake, consult your faction leader."));
        messagesFPermsUsage = ChatColor.translateAlternateColorCodes('&', getOrSet("messages.fperms-usage","/f perms group new <name> <priority> \n"+
                "/f perms group <name> add <player/chunk/group> <player/group>\n"+
                "/f perms group <name> blacklist <interaction> <block>\n"+
                "/f perms group list"));
    }


    private static Material parseMaterial(String string) {
        Material toReturn;
        if (NumberUtils.isNumber(string)) {
            int id = Integer.parseInt(string);
            toReturn = Material.getMaterial(id);
        } else {
            toReturn = Material.getMaterial(string);
        }
        return toReturn;
    }

    private static <T> T getOrSet(String path, T value) {

        return getOrSet(path, value, null);

    }

    private static <T> T getOrSet(String path, T value, Consumer<T> manipulation) {

        if (!config.contains(path)) {
            config.set(path, value);
            return value;
        }

        Object o = config.get(path);

        if (!value.getClass().isInstance(o)) {
            config.set(path, value);
            return value;
        }

        T t = (T) o;

        if (manipulation != null) {
            manipulation.accept(t);
            config.set(path, t);
        }

        return t;

    }

    public String getMessagesPlaceDeny() {
        return messagesPlaceDeny;
    }

    public String getMessagesInteractDeny() {
        return messagesInteractDeny;
    }

    public String getMessagesBreakDeny() {
        return messagesBreakDeny;
    }

    public String getMessagesFPermsUsage() {
        return messagesFPermsUsage;
    }
}
