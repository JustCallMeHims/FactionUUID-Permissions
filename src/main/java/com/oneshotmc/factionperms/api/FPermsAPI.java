package com.oneshotmc.factionperms.api;

import com.oneshotmc.factionperms.enums.InteractionType;
import com.oneshotmc.factionperms.objects.Group;
import com.oneshotmc.factionperms.objects.PFaction;
import com.oneshotmc.factionperms.objects.manager.PermissionsManager;
import com.oneshotmc.factionperms.regions.ChunkLocation;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class FPermsAPI {

    private static PermissionsManager manager = PermissionsManager.getInstance();

    public static boolean canPlaceCore(Player player){
        PFaction pFac = manager.getByPlayer(player);
        if(pFac == null)
            return false;

        List<Group> playerGroup = pFac.getGroups(player, false);

        for(Group group : playerGroup){
            if(group.canPlaceCore())
                return true;
        }

        return false;
    }

    public static boolean canUseGenBucket(Player player){
        PFaction pFac = manager.getByPlayer(player);
        if(pFac == null)
            return false;

        List<Group> playerGroup = pFac.getGroups(player, false);

        for(Group group : playerGroup){
            if(group.canUseGenBucket())
                return true;
        }

        return false;
    }

    public static boolean isBlacklist(Player player, ChunkLocation chunkLocation, InteractionType interaction, Material material) {
        PFaction pFac = manager.getByPlayer(player);
        if (pFac == null)
            return false;

        List<Group> groups = pFac.getGroups(player, false);

        for (Group g : groups) {
            if (g.isBlacklisted(interaction, material))
                return true;
        }

        List<Group> chunkGroups = pFac.getByChunk(chunkLocation);

        for (Group g : chunkGroups) {
            for(Group g2: groups){
                if((g.getAffectedGroups().contains(g2) || g.isPlayerAffected(player)) && g.isBlacklisted(interaction, material))
                    return true;
            }
        }

        return false;
    }


}
