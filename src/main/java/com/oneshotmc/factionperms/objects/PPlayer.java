package com.oneshotmc.factionperms.objects;

import org.bukkit.entity.Player;

import java.util.UUID;

public class PPlayer extends Permissions{

    public PPlayer(Player player) {
        this(player.getUniqueId());
    }

    public PPlayer(UUID playerId){
        super(playerId);
    }
}
