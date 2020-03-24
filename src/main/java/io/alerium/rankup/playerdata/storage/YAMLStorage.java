package io.alerium.rankup.playerdata.storage;

import io.alerium.rankup.RankupPlugin;
import io.alerium.rankup.playerdata.PlayerData;
import io.alerium.rankup.utils.configuration.YAMLConfiguration;

import java.util.UUID;

public class YAMLStorage implements Storage {
    
    private final RankupPlugin plugin = RankupPlugin.getInstance();
    
    private YAMLConfiguration config;
    
    @Override
    public void setup() throws Exception {
        config = new YAMLConfiguration(plugin, "playerdata");
    }

    @Override
    public PlayerData loadData(UUID uuid, String defaultRank) throws Exception {
        return new PlayerData(uuid, config.getConfig().getString(uuid + ".rank", defaultRank));
    }

    @Override
    public void saveData(PlayerData playerData) throws Exception {
        config.getConfig().set(playerData.getUuid() + ".rank", playerData.getRank());
        config.save();
    }

}
