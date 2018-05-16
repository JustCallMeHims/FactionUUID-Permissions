package com.oneshotmc.factionperms.objects;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.oneshotmc.factionperms.regions.ChunkLocation;
import com.oneshotmc.factionperms.util.GroupsUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PFaction {

    private final String id;

    private List<Group> groups = new ArrayList<>();
    private List<PPlayer> players = new ArrayList<>();

    public PFaction(Faction faction){
        this(faction.getId());
    }

    public PFaction(String id){
        this.id = id;
        groups.add(new Group(1, "Recruit"));
        groups.add(new Group(2, "Moderator"));
        groups.add(new Group(3, "Leader"));
    }

    public boolean addGroup(Group group, boolean force){
        Group g = getByName(group.getName());
        if(g != null) {
            if(!force)
                return false;

            groups.remove(g);
        }

        groups.add(group);
        return true;
    }

    public boolean delGroup(Group group){
        return groups.removeIf(g -> g.getId() == group.getId());
    }

    public boolean addPPlayer(PPlayer pPlayer){
        if(getPPlayer(pPlayer.getId()) != null)
            return false;

        players.add(pPlayer);
        return true;
    }

    public boolean delPPlayer(PPlayer pPlayer){
        return players.removeIf(p -> p.getId() == pPlayer.getId());
    }

    public PPlayer getPPlayer(Player player){
        return getPPlayer(player.getUniqueId());
    }

    public PPlayer getPPlayer(UUID playerId){
        return players.stream().filter(p -> p.getId() == playerId).findFirst().orElse(null);
    }

    public List<Group> getByChunk(ChunkLocation chunk){
        List<Group> groups = this.groups.stream().filter(g -> g.isAffectedChunk(chunk)).collect(Collectors.toList());
        return GroupsUtil.sortGroups(groups, true);
    }

    public Group getByName(String name){
        return this.groups.stream().filter(g -> g.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public List<Group> getGroups(Player player, boolean chunkGroups) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);

        List<Group> groups = this.groups.stream().filter(g -> g.isPlayerAffected(player) && (chunkGroups || !g.hasChunks())).collect(Collectors.toList());

        // add default groups to collection
        switch(fPlayer.getRole()){
            case NORMAL:
                groups.add(getByName("Recruit"));
                break;
            case MODERATOR:
                groups.add(getByName("Moderator"));
                break;
            default:
                groups.add(getByName("Leader"));
                break;
        }

        return groups;
    }


    public Faction getFaction(){
        return Factions.getInstance().getFactionById(this.id);
    }

}
