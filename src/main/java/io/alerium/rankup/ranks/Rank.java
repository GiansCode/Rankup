package io.alerium.rankup.ranks;

import io.alerium.rankup.RankupPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import pw.valaria.requirementsprocessor.RequirementsUtil;

import java.util.List;

@RequiredArgsConstructor @Getter
public class Rank {
    
    private final String id;
    private final String nextRank;
    
    private final ConfigurationSection requirements;
    private final List<String> actions;

    /**
     * This method checks if a Player has the requirements to execute the rank up
     * @param player The Player
     * @return True if the Player can rank up
     */
    public boolean canRankUp(Player player) {
        return RequirementsUtil.handle(player, requirements);
    }

    /**
     * This method executes the actions of the rank up
     * @param player The Player
     */
    public void executeRankUp(Player player) {
        RankupPlugin.getInstance().getActionUtil().executeActions(player, actions);
    }
    
}
