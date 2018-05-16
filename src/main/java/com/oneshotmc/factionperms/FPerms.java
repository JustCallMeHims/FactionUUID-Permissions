package com.oneshotmc.factionperms;


import com.oneshotmc.factionperms.commands.functions.CommandHandler;
import com.oneshotmc.factionperms.configuration.Configuration;
import com.oneshotmc.factionperms.listeners.FactionActionListener;
import com.oneshotmc.factionperms.listeners.InteractionListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class FPerms extends JavaPlugin{

    private File configFile;
    private Configuration configuration;

    public void onEnable(){
        configFile = new File(getDataFolder(), "settings.yml");
        configFile.getParentFile().mkdirs();
        try {
            configFile.createNewFile();
            configuration = new Configuration(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        registerListener(new FactionActionListener());
        registerListener(new CommandHandler(configuration));
        registerListener(new InteractionListener(configuration));
    }

    public void registerListener(Listener listener){
        getServer().getPluginManager().registerEvents(listener, this);
    }

}
