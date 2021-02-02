package me.lucyy.phantomrepellent;

import org.bukkit.Bukkit;
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
    private int leftDuration;
    private final UUID playerId;
    private final BossBar bar;
    private final EffectManager manager;
    private final NamespacedKey key;

    public RepellentEffect(EffectManager manager, int totalDuration, UUID playerId, NamespacedKey key) {
        this.totalDuration = totalDuration;
        this.leftDuration = totalDuration;
        this.playerId = playerId;
        this.manager = manager;
        this.key = key;
        bar = Bukkit.createBossBar("title", BarColor.WHITE, BarStyle.SOLID);
        bar.addPlayer(Objects.requireNonNull(Bukkit.getPlayer(playerId)));
        makeCatRidePlayer();
    }

    // this is easily the weirdest method name i've ever written
    @SuppressWarnings("ConstantConditions") // this runnable will be destroyed should the player leave
    public void makeCatRidePlayer() {
        Player player = Bukkit.getPlayer(playerId);
        Cat entity = (Cat) player.getWorld().spawnEntity(player.getLocation(), EntityType.CAT);
        //entity.setInvisible(true);
        entity.setSilent(true);
        entity.setAI(false);
        entity.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte)1);
        player.addPassenger(entity);
    }

    @SuppressWarnings("ConstantConditions") // this runnable will be destroyed should the player leave
    private void removeRidingCats() {
        Player player = Bukkit.getPlayer(playerId);
        for (Entity pass : player.getPassengers()) {
            if (pass.getPersistentDataContainer().has(key, PersistentDataType.BYTE)) {
                pass.remove();
            }
        }
    }

    @Override
    public void cancel() {
        removeRidingCats();
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
        bar.setProgress( (double)leftDuration / (double) totalDuration );
        bar.setTitle("Remaining time " + leftDuration);
    }
}
