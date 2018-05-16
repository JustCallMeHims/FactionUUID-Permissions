package com.oneshotmc.factionperms.listeners;

import com.massivecraft.factions.event.FPlayerLeaveEvent;
import com.massivecraft.factions.event.FactionDisbandEvent;
import com.massivecraft.factions.event.LandUnclaimEvent;
import com.oneshotmc.factionperms.objects.PFaction;

import com.oneshotmc.factionperms.objects.PPlayer;
import com.oneshotmc.factionperms.objects.manager.PermissionsManager;
import com.oneshotmc.factionperms.regions.ChunkLocation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class FactionActionListener implements Listener {

    private PermissionsManager manager = PermissionsManager.getInstance();

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDisband(FactionDisbandEvent event){
        PFaction pFac = manager.getById(event.getFaction().getId());
        if(pFac == null)
            return;

        manager.delFaction(pFac);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onLandUnclaim(LandUnclaimEvent event){
        PFaction pFac = manager.getById(event.getFaction().getId());

        if(pFac == null)
            return;

        ChunkLocation chunk = new ChunkLocation((int)event.getLocation().getX(),(int) event.getLocation().getZ());
        pFac.getByChunk(chunk).forEach(g -> g.getAffectedChunks().remove(chunk));
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onMemberChange(FPlayerLeaveEvent event){
        //We already handle disbandment so....
        if(event.getReason() == FPlayerLeaveEvent.PlayerLeaveReason.DISBAND)
            return;

        Player player = event.getfPlayer().getPlayer();
        PFaction pFac = manager.getByPlayer(event.getfPlayer().getPlayer());
        if(pFac == null)
            return;

        pFac.getGroups(player, true).forEach(g -> g.getAffectedPlayers().remove(player.getUniqueId()));
        PPlayer pPlayer = pFac.getPPlayer(player);
        if(pPlayer == null)
            return;

        pFac.delPPlayer(pPlayer);
    }
}
