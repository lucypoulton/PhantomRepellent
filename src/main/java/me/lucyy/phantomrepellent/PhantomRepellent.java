package me.lucyy.phantomrepellent;

import me.lucyy.phantomrepellent.command.GivePotionCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class PhantomRepellent extends JavaPlugin {
    private final EffectManager manager;

    public PhantomRepellent() {
        manager = new EffectManager(this);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
	public void onEnable() {
        getCommand("givepotion").setExecutor(new GivePotionCommand(this));
        getServer().getPluginManager().registerEvents(manager, this);
    }

    public static String formatTime(int duration) {
    	return duration / 60 + ":" + String.format("%02d", duration % 60);
	}

    public EffectManager getManager() {
        return manager;
    }
}
