package io.alerium.rankup.integrations;

import io.alerium.rankup.RankupPlugin;
import io.alerium.rankup.playerdata.PlayerData;
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
        if (!params.equalsIgnoreCase("rank"))
            return null;

        PlayerData playerData = plugin.getPlayerDataManager().getPlayer(player.getUniqueId());
        return playerData.getRank();
    }
}
