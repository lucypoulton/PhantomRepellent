package me.lucyy.phantomrepellent;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.UUID;

public class RepellentEffect extends BukkitRunnable {
    private final int totalDuration;
    private int leftDuration;
    private final UUID playerId;
    private final BossBar bar;
    private final EffectManager manager;

    public RepellentEffect(EffectManager manager, int totalDuration, UUID playerId) {
        this.totalDuration = totalDuration;
        this.playerId = playerId;
        this.manager = manager;
        bar = Bukkit.createBossBar("title", BarColor.WHITE, BarStyle.SOLID);
        bar.addPlayer(Objects.requireNonNull(Bukkit.getPlayer(playerId)));
    }

    @Override
    public void run() {
        leftDuration--;
        if (leftDuration == 0) {
            manager.removeAll(playerId);
            return;
        }
        bar.setProgress( (double)leftDuration / (double) totalDuration );
        bar.setTitle("Remaining time " + leftDuration);
    }
}
