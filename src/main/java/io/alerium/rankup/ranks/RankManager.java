package io.alerium.rankup.ranks;

import io.alerium.rankup.RankupPlugin;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class RankManager {
    
    private final RankupPlugin plugin = RankupPlugin.getInstance();
    
    private final List<Rank> ranks = new ArrayList<>();
    @Getter private String firstRank;
    @Getter private String lastRank;

    /**
     * This method enables the RankManager
     */
    public void enable() {
        loadRanks();
    }

    /**
     * This method gets a Rank
     * @param id The Rank ID
     * @return The Rank, null if not found
     */
    public Rank getRank(String id) {
        for (Rank rank : ranks) {
            if (rank.getId().equalsIgnoreCase(id))
                return rank;
        }
        
        return null;
    }

    /**
     * This method loads all the ranks from the config
     */
    public void loadRanks() {
        ranks.clear();
        
        ConfigurationSection section = plugin.getConfiguration().getConfig().getConfigurationSection("ranks");
        for (String id : section.getKeys(false)) {
            ConfigurationSection rankSection = section.getConfigurationSection(id);

            String nextRank = rankSection.getString("next-rank");
            ConfigurationSection requirements = rankSection.getConfigurationSection("requirements");
            List<String> actions = rankSection.getStringList("actions");

            ranks.add(new Rank(id, nextRank, requirements, actions));
        }

        plugin.getLogger().info("Loaded " + ranks.size() + " ranks.");

        firstRank = plugin.getConfiguration().getConfig().getString("rank-settings.first-rank");
        lastRank = plugin.getConfiguration().getConfig().getString("rank-settings.last-rank");
    }
    
}
