package io.alerium.rankup;

import io.alerium.rankup.commands.RankupAdminCommand;
import io.alerium.rankup.commands.RankupCommand;
import io.alerium.rankup.integrations.PlaceholderAPI;
import io.alerium.rankup.playerdata.PlayerDataManager;
import io.alerium.rankup.playerdata.listeners.PlayerDataListener;
import io.alerium.rankup.ranks.RankManager;
import io.alerium.rankup.utils.configuration.YAMLConfiguration;
import io.samdev.actionutil.ActionUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RankupPlugin extends JavaPlugin {

    @Getter private static RankupPlugin instance;
    
    @Getter private YAMLConfiguration configuration;
    @Getter private YAMLConfiguration messages;
    
    @Getter private ActionUtil actionUtil;
    
    @Getter private RankManager rankManager;
    @Getter private PlayerDataManager playerDataManager;
    
    @Override
    public void onEnable() {
        instance = this;
        long time = System.currentTimeMillis();
        
        registerConfigs();
        registerInstances();
        registerListeners();
        registerCommands();
        registerIntegrations();
        
        getLogger().info("Plugin enabled in " + (System.currentTimeMillis()-time) + "ms");
    }

    @Override
    public void onDisable() {
        playerDataManager.saveAllPlayers(true);
    }
    
    private void registerConfigs() {
        configuration = new YAMLConfiguration(this, "config");
        messages = new YAMLConfiguration(this, "messages");
    }
    
    private void registerInstances() {
        rankManager = new RankManager();
        rankManager.enable();
        
        playerDataManager = new PlayerDataManager();
        playerDataManager.enable();
        
        actionUtil = ActionUtil.init(this);
    }
    
    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        
        pm.registerEvents(new PlayerDataListener(), this);
    }
    
    private void registerCommands() {
        getCommand("rankup").setExecutor(new RankupCommand());
        getCommand("adminrankup").setExecutor(new RankupAdminCommand());
    }
    
    private void registerIntegrations() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            new PlaceholderAPI().register();
    }
    
}
