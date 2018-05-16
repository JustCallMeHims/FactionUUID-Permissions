package com.oneshotmc.factionperms.objects;


import com.oneshotmc.factionperms.regions.ChunkLocation;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Group extends Permissions {

    private String name;
    private int priority;
    private List<UUID> affectedPlayers = new ArrayList();
    private List<ChunkLocation> affectedChunks = new ArrayList<>();
    private List<Group> affectedGroups = new ArrayList<>();

    public Group(int priority, String name) {
        this(UUID.randomUUID(), priority, name);
    }

    public Group(UUID uuid, int priority, String name) {
        super(uuid);
        this.name = name;
        this.priority = priority;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public List<UUID> getAffectedPlayers() {
        return affectedPlayers;
    }

    public boolean isPlayerAffected(Player player) {
        return affectedPlayers.contains(player.getUniqueId());
    }

    public void addPlayer(Player player) {
        affectedPlayers.add(player.getUniqueId());
    }

    public List<ChunkLocation> getAffectedChunks() {
        return affectedChunks;
    }

    public boolean isAffectedChunk(ChunkLocation chunkLocation) {
        return affectedChunks.contains(chunkLocation);
    }

    public boolean addChunk(ChunkLocation chunk) {
        if (isAffectedChunk(chunk))
            return false;

        return affectedChunks.add(chunk);
    }

    public List<Group> getAffectedGroups(){
        return affectedGroups;
    }

    public boolean isAffectedGroup(Group group){
        return affectedGroups.contains(group);
    }

    public boolean addGroup(Group group){
        if(equals(group) || isAffectedGroup(group))
            return false;

        return affectedGroups.add(group);
    }

    public boolean hasChunks(){
        return affectedChunks.size() > 0;
    }

}
