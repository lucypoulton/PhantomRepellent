package me.lucyy.phantomrepellent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.UUID;

public class RepellentEffect extends BukkitRunnable {
    private final int totalDuration;

    public int getLeftDuration() {
        return leftDuration;
    }

    private int leftDuration;
    private final UUID playerId;
    private final BossBar bar;
    private final EffectManager manager;

    @SuppressWarnings("ConstantConditions")
    public RepellentEffect(EffectManager manager, int totalDuration, UUID playerId) {
        this.totalDuration = totalDuration;
        this.leftDuration = totalDuration;
        this.playerId = playerId;
        this.manager = manager;
        bar = Bukkit.createBossBar("title", BarColor.BLUE, BarStyle.SOLID);
        bar.addPlayer(Objects.requireNonNull(Bukkit.getPlayer(playerId)));

        for (Phantom entity : Bukkit.getPlayer(playerId).getWorld().getEntitiesByClass(Phantom.class)) {
            if (entity.getTarget() != null && entity.getTarget().getUniqueId().equals(playerId)) entity.setTarget(null);
        }
    }

    @Override
    public void cancel() {
        bar.removeAll();
        super.cancel();

    }

    @Override
    public void run() {
        leftDuration--;
        if (leftDuration <= 0) {
            manager.removeAll(playerId);
            return;
        }
        bar.setProgress((double) leftDuration / (double) totalDuration);
        bar.setTitle("Your Phantom Repellent Potion has " + PhantomRepellent.formatTime(leftDuration) + " remaining");
    }
}
