package com.oneshotmc.factionperms.commands.functions;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.oneshotmc.factionperms.commands.SGroup;
import com.oneshotmc.factionperms.configuration.Configuration;
import com.oneshotmc.factionperms.objects.manager.PermissionsManager;
import com.oneshotmc.factionperms.objects.PFaction;
import com.oneshotmc.factionperms.util.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandHandler extends CmdMap implements Listener{

    private Configuration configuration;

    public CommandHandler(Configuration configuration){
        this.configuration = configuration;
        super.addSub(new SGroup(this.configuration));
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        if(event.isCancelled())
            return;

        if(!event.getMessage().toLowerCase().startsWith("/f perms"))
            return;

        event.setCancelled(true);

        String[] args = event.getMessage().split(" ");
        noInstance(event.getPlayer());

        if(args.length == 2){
            ChatUtil.sendMessage(event.getPlayer(), "&eFaction Permissions [&9Help Menu&e]!");
            return;
        }

        String command = null;
        String[] output = new String[args.length-3 > -1 ? args.length-3 : 0];

        for(int i=2; i<args.length; i++){
            if(i == 2){
                command = args[i];
                continue;
            }

            output[i-3] = args[i];
        }


        super.performSub(event.getPlayer(), command, output);
    }


    private void noInstance(Player player){
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        if(!fPlayer.hasFaction()){
            return;
        }
        // Main command is here, / f perms
        PFaction pFaction = PermissionsManager.getInstance().getByPlayer(player);
        if(pFaction == null){
            PermissionsManager.getInstance().newPFaction(fPlayer.getFaction());
        }
    }

}
