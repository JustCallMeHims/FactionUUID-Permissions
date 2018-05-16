package com.oneshotmc.factionperms.commands.functions;

import org.bukkit.entity.Player;

public abstract class SubCommand {

    private static final String NO_PERMISSION = "&cInsufficient Permissions";

    private String name;
    private String permission;
    private String[] aliases;

    public SubCommand(String name, String permission, String... aliases){
        this.name = name;
        this.permission = permission;
        this.aliases = aliases;
    }


    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public String[] getAliases() {
        return aliases;
    }

    public boolean hasAliases(String alias){
        for(String s: aliases){
            if(s.equalsIgnoreCase(alias))
                return true;
        }

        return false;
    }

    public static String getNoPermission(){
        return NO_PERMISSION;
    }

    public abstract void onCommand(Player player, String[] args);
}
