package me.lucyy.phantomrepellent.command;

import me.lucyy.phantomrepellent.PhantomRepellent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GivePotionCommand implements CommandExecutor {
    private PhantomRepellent plugin;

    public GivePotionCommand(PhantomRepellent plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	if (args.length != 2) return false;
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) return false;

        int duration;
        try {
            duration = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return false;
        }

        if (duration <= 0) return false;

        target.getInventory().addItem(plugin.getManager().getPotion(duration));
        sender.sendMessage("Potion given successfully");
        return true;
    }
}
