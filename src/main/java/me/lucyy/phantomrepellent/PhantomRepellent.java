package me.lucyy.phantomrepellent;

import me.lucyy.phantomrepellent.command.ForceInsomniaCommand;
import me.lucyy.phantomrepellent.command.GivePotionCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class PhantomRepellent extends JavaPlugin {
    private final EffectManager manager;

    public PhantomRepellent() {
        manager = new EffectManager(this);
    }
    @Override
    public void onEnable() {
        getCommand("insomnia").setExecutor(new ForceInsomniaCommand());
        getCommand("givepotion").setExecutor(new GivePotionCommand(this));
        getServer().getPluginManager().registerEvents(manager, this);
    }

    public EffectManager getManager() {
        return manager;
    }
}
