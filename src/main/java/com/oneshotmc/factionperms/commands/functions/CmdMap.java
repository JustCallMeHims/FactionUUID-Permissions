package com.oneshotmc.factionperms.commands.functions;

import com.oneshotmc.factionperms.util.ChatUtil;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public abstract class CmdMap {

    private Set<SubCommand> subCommands = new HashSet<>();

    public void addSub(SubCommand subCommand){
        subCommands.add(subCommand);
    }

    public boolean performSub(Player player, String name, String[] args){
        SubCommand subCommand = subCommands.stream().filter(s -> s.getName().equalsIgnoreCase(name) ||
        s.hasAliases(name)).findFirst().orElse(null);

        if(subCommand == null)
            return false;

        if(!player.hasPermission(subCommand.getPermission())){
            ChatUtil.sendMessage(player, SubCommand.getNoPermission());
            return true;
        }

        subCommand.onCommand(player, args);
        return true;
    }


}
