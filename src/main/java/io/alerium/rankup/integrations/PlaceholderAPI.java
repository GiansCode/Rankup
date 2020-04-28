package io.alerium.rankup.integrations;

import io.alerium.rankup.RankupPlugin;
import io.alerium.rankup.playerdata.PlayerData;
import io.alerium.rankup.ranks.Rank;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceholderAPI extends PlaceholderExpansion {
    
    private final RankupPlugin plugin = RankupPlugin.getInstance();
    
    @Override
    public String getIdentifier() {
        return "rankup";
    }

    @Override
    public String getAuthor() {
        return "xQuickGlare";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        PlayerData playerData = plugin.getPlayerDataManager().getPlayer(player.getUniqueId());
        switch (params.toUpperCase()) {
            case "RANK":
                return playerData.getRank();
            case "NEXT_RANK":
                Rank rank = plugin.getRankManager().getRank(playerData.getRank());
                if (rank == null)
                    return null;
                return rank.getNextRank();
            default:
                return plugin.getMessages().getMessage("nonePlaceholder").format().toString();
        }
    }
}
