package com.oneshotmc.factionperms.commands;

import com.oneshotmc.factionperms.commands.functions.SubCommand;
import com.oneshotmc.factionperms.configuration.Configuration;
import com.oneshotmc.factionperms.enums.InteractionType;
import com.oneshotmc.factionperms.objects.manager.PermissionsManager;
import com.oneshotmc.factionperms.regions.ChunkLocation;
import com.oneshotmc.factionperms.objects.Group;
import com.oneshotmc.factionperms.objects.PFaction;
import com.oneshotmc.factionperms.util.ChatUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

/**
 *
 */
public class SGroup extends SubCommand{

    private Configuration configuration;
    public SGroup(Configuration configuration) {
        super("group", "fperms.group", "groep");
        this.configuration = configuration;
    }

    @Override
    public void onCommand(Player player, String[] args) {
        PFaction pFaction = PermissionsManager.getInstance().getByPlayer(player);

        if(args.length == 0) {
            ChatUtil.sendMessage(player, configuration.getMessagesFPermsUsage());
            return;
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("list")){
            List<Group> group = pFaction.getGroups(player, true);
            //TODO: Display group
            return;
        }

        if(args.length == 3 && args[0].equalsIgnoreCase("new")){
            int priority = Integer.valueOf(args[2]);
            String name = args[1];

            Group group = new Group(priority, name);
            pFaction.addGroup(group, false);

            ChatUtil.sendMessage(player, "&aGroup '"+group.getName()+"' has been created!");
            return;
        }

        Group group = pFaction.getByName(args[0]);

        if(group == null){
            ChatUtil.sendMessage(player, "Group '"+args[0]+"' does not exist");
            return;
        }

        if(args[1].equalsIgnoreCase("add")){
            if(args[2].equalsIgnoreCase("c") || args[2].equalsIgnoreCase("chunk")){
                Location loc = player.getLocation();
                group.addChunk(new ChunkLocation(loc));
                ChatUtil.sendMessage(player, "&aAdded &7X: " + (loc.getBlockX() >> 4) + " Z: " + (loc.getBlockZ() >> 4) + " &ato '"+group.getName()+"'");
                return;
            }

            if(args[2].equalsIgnoreCase("p") || args[2].equalsIgnoreCase("player")){
                Player target = Bukkit.getPlayer(args[3]);
                group.addPlayer(target);

                ChatUtil.sendMessage(player, "&aAdded &7" + target.getName() + " &ato '"+group.getName()+"'");
                return;
            }

            if(args[2].equalsIgnoreCase("g") || args[2].equalsIgnoreCase("group")){
                PFaction pFac = PermissionsManager.getInstance().getByPlayer(player);
                if(pFac == null)
                    return;
                Group target = pFac.getByName(args[3]);
                group.addGroup(target);

                ChatUtil.sendMessage(player, "&aAdded &7" + target.getName() + " &ato '"+group.getName()+"'");
                return;
            }

            return;
        }

        if(args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("remove")){
            if(args[2].equalsIgnoreCase("c") || args[2].equalsIgnoreCase("chunk")){
                Location loc = player.getLocation();
                ChunkLocation chunkLocation = new ChunkLocation(loc);
                group.getAffectedChunks().removeIf(chunkLocation::equals);
                ChatUtil.sendMessage(player, "&aRemoved &7X: " + (loc.getBlockX() >> 4) + " Z: " + (loc.getBlockZ() >> 4) + " &afrom '"+group.getName()+"'");
                return;
            }

            if(args[2].equalsIgnoreCase("p") || args[2].equalsIgnoreCase("player")){
                Player target = Bukkit.getPlayer(args[3]);
                group.getAffectedPlayers().removeIf(target.getUniqueId()::equals);

                ChatUtil.sendMessage(player, "&aDeleted &7" + target.getName() + " &afrom '"+group.getName()+"'");
                return;
            }

            if(args[2].equalsIgnoreCase("g") || args[2].equalsIgnoreCase("group")){
                PFaction pFac = PermissionsManager.getInstance().getByPlayer(player);
                if(pFac == null)
                    return;
                Group target = pFac.getByName(args[3]);
                group.getAffectedGroups().removeIf(target::equals);

                ChatUtil.sendMessage(player, "&aDeleted &7" + target.getName() + " &afrom '"+group.getName()+"'");
                return;
            }

            return;
        }

        if(args[1].equalsIgnoreCase("blacklist") || args[1].equalsIgnoreCase("bl")){
            InteractionType interaction = InteractionType.valueOf(args[3].toUpperCase());
            String materialString = args[4].toUpperCase();

            Material material;
            if(NumberUtils.isNumber(materialString)){
                int materialID = Integer.parseInt(materialString);
                material = Material.getMaterial(materialID);
            }
            else{
                material = Material.getMaterial(materialString.toUpperCase());
            }

            if(material == null){
                ChatUtil.sendMessage(player,"Material '"+materialString+"' does not exist");
                return;
            }

            if(args[2].equalsIgnoreCase("add")){
                group.getList(interaction).add(material);
                ChatUtil.sendMessage(player, "&cBlacklist &l&c>> &aadded: &7"+ material.name() +
                        " &ato: &7"+interaction.name() + " &ain: &7" + group.getName());
                return;
            }

            if(args[2].equalsIgnoreCase("del")){
                group.getList(interaction).remove(material);
                ChatUtil.sendMessage(player, "&cBlacklist &l&c>> &aadded: &7"+ material.name() +
                        " &ato: &7"+interaction.name() + " &ain: &7" + group.getName());
                return;
            }

        }

        ChatUtil.sendMessage(player, configuration.getMessagesFPermsUsage());
        return;

    }
}
