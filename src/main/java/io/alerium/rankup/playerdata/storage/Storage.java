package io.alerium.rankup.playerdata.storage;

import io.alerium.rankup.playerdata.PlayerData;

import java.util.UUID;

public interface Storage {

    /**
     * This method setups the Storage
     * @throws Exception
     */
    void setup() throws Exception;

    /**
     * This method loads the PlayerData from the Storage
     * @param uuid The UUID of the Player
     * @return The PlayerData object
     * @throws Exception
     */
    PlayerData loadData(UUID uuid, String defaultRank) throws Exception;

    /**
     * This method saves a PlayerData in the Storage
     * @param playerData The PlayerData
     * @throws Exception
     */
    void saveData(PlayerData playerData) throws Exception;
    
}
