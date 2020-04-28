package io.alerium.rankup.playerdata;

import io.alerium.rankup.RankupPlugin;
import io.alerium.rankup.playerdata.storage.MYSQLStorage;
import io.alerium.rankup.playerdata.storage.Storage;
import io.alerium.rankup.playerdata.storage.YAMLStorage;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerDataManager {
    
    private final RankupPlugin plugin = RankupPlugin.getInstance();
    private final List<PlayerData> players = new ArrayList<>();
    
    private Storage storage;

    /**
     * This method enables the PlayerDataManager
     */
    public void enable() {
        String storageType = plugin.getConfiguration().getConfig().getString("storage.type").toUpperCase();
        if ("MYSQL".equals(storageType)) {
            storage = new MYSQLStorage();
        } else {
            storage = new YAMLStorage();
        }

        try {
            storage.setup();
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "An error occurred while the database setup, stopping the plugin.", e);
            Bukkit.getPluginManager().disablePlugin(plugin);
        }

        Bukkit.getOnlinePlayers().forEach(player -> loadPlayer(player.getUniqueId()));
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> saveAllPlayers(false), 20*60*5, 20*60*5);
    }

    /**
     * This method saves all the loaded PlayerData to the Storage defined in the config
     * @param remove True if the PlayerData(s) has to be removed from the cache
     */
    public void saveAllPlayers(boolean remove) {
        for (PlayerData player : players.toArray(new PlayerData[0]))
            savePlayer(player, remove);
    }

    /**
     * This method loads a PlayerData from the Storage
     * @param uuid The UUID of the Player
     */
    public void loadPlayer(UUID uuid) {
        try {
            players.add(storage.loadData(uuid, plugin.getRankManager().getFirstRank()));
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "An error occurred while loading " + uuid + " player data", e);
        }
    }

    /**
     * This method saves a PlayerData in the Storage
     * @param uuid The UUID of the Player
     * @param remove True if the PlayerData has to be removed from the cache
     */
    public void savePlayer(UUID uuid, boolean remove) {
        PlayerData playerData = getPlayer(uuid);
        if (playerData == null)
            return;

        savePlayer(playerData, remove);
    }

    /**
     * This method saves a PlayerData in the Storage
     * @param playerData The PlayerData
     * @param remove True if the PlayerData has to be removed from the cache
     */
    public void savePlayer(PlayerData playerData, boolean remove) {
        if (playerData == null || playerData.getUuid() == null)
            return;
        
        try {
            storage.saveData(playerData);
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "An error occurred while saving player data of " + playerData.getUuid(), e);
        }

        if (remove)
            players.remove(playerData);
    }

    /**
     * This method gets a PlayerData from the UUID of the Player
     * @param uuid The UUID of the Player
     * @return The PlayerData object, null if not found
     */
    public PlayerData getPlayer(UUID uuid) {
        for (PlayerData player : players) {
            if (player.getUuid().equals(uuid))
                return player;
        }

        return null;
    }
    
}
