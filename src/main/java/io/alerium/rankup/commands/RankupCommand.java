package io.alerium.rankup.commands;

import io.alerium.rankup.RankupPlugin;
import io.alerium.rankup.playerdata.PlayerData;
import io.alerium.rankup.ranks.Rank;
import io.alerium.rankup.utils.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankupCommand extends Command {
    
    private final RankupPlugin plugin = RankupPlugin.getInstance();
    
    public RankupCommand() {
        super("rankup.commands.rankup", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        PlayerData playerData = plugin.getPlayerDataManager().getPlayer(player.getUniqueId());
        
        if (playerData.getRank().equalsIgnoreCase(plugin.getRankManager().getLastRank())) {
            plugin.getMessages().getMessage("commands.rankup.lastRankReached").format().send(player);
            return;
        }

        Rank rank = plugin.getRankManager().getRank(playerData.getRank());
        if (!rank.canRankUp(player)) {
            plugin.getMessages().getMessage("commands.rankup.noRequirements").format().send(player);
            return;
        }
        
        rank.executeRankUp(player);
        playerData.setRank(rank.getNextRank());
        plugin.getMessages().getMessage("commands.rankup.done")
                .addPlaceholder("rank", rank.getNextRank())
                .format()
                .send(player);
    }
    
}
