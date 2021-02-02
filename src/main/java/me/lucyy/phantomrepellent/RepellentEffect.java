package me.lucyy.phantomrepellent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
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
    private final NamespacedKey key;
    private final Cat cat;

    @SuppressWarnings("ConstantConditions")
    public RepellentEffect(EffectManager manager, int totalDuration, UUID playerId, NamespacedKey key) {
        this.totalDuration = totalDuration;
        this.leftDuration = totalDuration;
        this.playerId = playerId;
        this.manager = manager;
        this.key = key;
        bar = Bukkit.createBossBar("title", BarColor.BLUE, BarStyle.SOLID);
        bar.addPlayer(Objects.requireNonNull(Bukkit.getPlayer(playerId)));

        Player player = Bukkit.getPlayer(playerId);

        cat = (Cat) player.getWorld().spawnEntity(player.getLocation(), EntityType.CAT);
        cat.setInvisible(true);
        cat.setSilent(true);
        cat.setAI(false);
        cat.setInvulnerable(true);
        cat.setCollidable(false);
        cat.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte)1);
    }


    @Override
    public void cancel() {
        cat.remove();
        bar.removeAll();
        super.cancel();

    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void run() {
        leftDuration--;
        if (leftDuration <= 0) {
            manager.removeAll(playerId);
            return;
        }
        bar.setProgress( (double)leftDuration / (double) totalDuration );
        bar.setTitle("Your Phantom Repellent Potion has " + PhantomRepellent.formatTime(leftDuration) +  " remaining");
        cat.teleport(Bukkit.getPlayer(playerId).getLocation().add(0, 2, 0));
    }
}
