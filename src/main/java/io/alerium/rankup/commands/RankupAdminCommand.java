package io.alerium.rankup.commands;

import io.alerium.rankup.RankupPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RankupAdminCommand implements CommandExecutor {

    private final RankupPlugin plugin = RankupPlugin.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("rankup.admin")) {
            plugin.getMessages().getMessage("commands.noPermission").format().send(sender);
            return true;
        }
        
        if (args.length == 0) {
            plugin.getMessages().getMessage("commands.admin.help").format().send(sender);
            return true;
        }
        
        switch (args[0].toUpperCase()) {
            case "RELOAD":
                plugin.getConfiguration().reload();
                plugin.getMessages().reload();
                plugin.getRankManager().loadRanks();
                plugin.getMessages().getMessage("commands.admin.reload").format().send(sender);
                break;
            
            default:
                plugin.getMessages().getMessage("commands.admin.help").format().send(sender);
                break;
        }
        return true;
    }
}
