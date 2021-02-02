package me.lucyy.phantomrepellent.command;

import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceInsomniaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        player.setStatistic(Statistic.TIME_SINCE_REST, 999999999);
        player.sendMessage("Statistic set");
        return true;
    }
}
