package com.oneshotmc.factionperms.objects.manager;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.oneshotmc.factionperms.objects.PFaction;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PermissionsManager {

    private static PermissionsManager instance;
    // feel like using a map for this one, probably can do a collection and use Lambda
    private Map<String, PFaction> factions = new HashMap();

    /*
    Lazy Singleton. Initiliaze.
     */
    public static PermissionsManager getInstance(){
        if(instance == null) instance = new PermissionsManager();
        return instance;
    }

    public PFaction getById(String factionId){
        return factions.get(factionId);
    }


    public PFaction getByName(String factionName){
        return factions.get(Factions.getInstance().getByTag(factionName).getId());
    }

    public PFaction getByPlayer(Player player){
        FPlayer fP = FPlayers.getInstance().getByPlayer(player);
        return getById(fP.getFactionId());
    }

    public PFaction newPFaction(Faction faction){
        PFaction pFaction = new PFaction(faction);
        factions.put(faction.getId(), pFaction);
        return pFaction;
    }

    public void addFaction(PFaction pFaction){
        factions.put(pFaction.getFaction().getId(), pFaction);
    }

    public void delFaction(PFaction pFaction){
        factions.remove(pFaction.getFaction().getId());
    }
}
