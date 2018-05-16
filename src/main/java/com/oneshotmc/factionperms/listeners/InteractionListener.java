package com.oneshotmc.factionperms.listeners;

import com.oneshotmc.factionperms.api.FPermsAPI;
import com.oneshotmc.factionperms.configuration.Configuration;
import com.oneshotmc.factionperms.regions.ChunkLocation;
import com.oneshotmc.factionperms.enums.InteractionType;
import com.oneshotmc.factionperms.util.ChatUtil;
import com.oneshotmc.factionperms.util.FactionsUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractionListener implements Listener{

    private Configuration configuration;

    public InteractionListener(Configuration configuration){
        this.configuration = configuration;
    }


    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBucketFill(PlayerBucketFillEvent event){
        Player player = event.getPlayer();
        Block fluidBlock = event.getBlockClicked().getRelative(event.getBlockFace());
        if(isBlacklisted(player, InteractionType.BREAK, fluidBlock.getType(), fluidBlock.getLocation())){
            ChatUtil.sendMessage(player, configuration.getMessagesBreakDeny().replaceAll("%material%",ChatUtil.readableMaterial(fluidBlock.getType())));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBucketEmpty(PlayerBucketEmptyEvent event){
        Block placeBlock = event.getBlockClicked().getRelative(event.getBlockFace());
        Player player = event.getPlayer();
        Material typeSet;
        switch (event.getBucket()){
            case WATER_BUCKET:
                typeSet = Material.WATER;
                break;
            case LAVA_BUCKET:
                typeSet = Material.LAVA;
                break;
            default:
                return;
               // throw new InternalException("The bucket emptied is of type "+event.getBucket()+" and can thus not be handled");
        }
        if(isBlacklisted(player, InteractionType.BREAK, placeBlock.getType(), placeBlock.getLocation())){
            ChatUtil.sendMessage(player, configuration.getMessagesBreakDeny().replaceAll("%material%",ChatUtil.readableMaterial(placeBlock.getType())));
            event.setCancelled(true);
            return;
        }
        if(isBlacklisted(player, InteractionType.PLACE, typeSet, placeBlock.getLocation())){
            ChatUtil.sendMessage(player, configuration.getMessagesPlaceDeny().replaceAll("%material%",ChatUtil.readableMaterial(typeSet)));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event){
        Block block = event.getBlock();
        if(!FactionsUtil.isInTerritory(block.getLocation()))
            return;

        Player player = event.getPlayer();

        if(isBlacklisted(player, InteractionType.BREAK, block.getType(),block.getLocation())) {
            ChatUtil.sendMessage(player, configuration.getMessagesBreakDeny().replaceAll("%material%",ChatUtil.readableMaterial(block.getType())));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event){

        Block blockPlaced = event.getBlock();
        if(!FactionsUtil.isInTerritory(blockPlaced.getLocation()))
            return;

        Player player = event.getPlayer();

        Material previousType = event.getBlockReplacedState().getType();
        if(isBlacklisted(player, InteractionType.BREAK, previousType, blockPlaced.getLocation())){
            ChatUtil.sendMessage(player, configuration.getMessagesBreakDeny().replaceAll("%material%",ChatUtil.readableMaterial(previousType)));
            event.setCancelled(true);
            return;
        }

        if(isBlacklisted(player, InteractionType.PLACE, blockPlaced.getType(),blockPlaced.getLocation())) {
            ChatUtil.sendMessage(player, configuration.getMessagesPlaceDeny().replaceAll("%material%",ChatUtil.readableMaterial(blockPlaced.getType())));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event){
        Block clickedBlock = event.getClickedBlock();
        Location location = clickedBlock.getLocation();
        if(!FactionsUtil.isInTerritory(location) || clickedBlock.getType() == Material.AIR || event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        Player player = event.getPlayer();

        // To prevent items such as creeper eggs
        Material type = event.getItem().getType();
        if(isBlacklisted(player,InteractionType.PLACE, type,location)){
            ChatUtil.sendMessage(player, configuration.getMessagesInteractDeny().replaceAll("%material%",ChatUtil.readableMaterial(type)));
            event.setCancelled(true);
            return;
        }

        if(!isBlacklisted(player, InteractionType.INTERACT, clickedBlock.getType(), location))
            return;

        event.setCancelled(true);
    }

    private boolean isBlacklisted(Player player, InteractionType interaction, Material material, Location location){
        ChunkLocation chunkLocation = new ChunkLocation(location);
        return FPermsAPI.isBlacklist(player, chunkLocation, interaction, material);
    }

}
