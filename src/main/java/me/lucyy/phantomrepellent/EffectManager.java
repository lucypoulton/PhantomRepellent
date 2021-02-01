package me.lucyy.phantomrepellent;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class EffectManager implements Listener {
    private final PhantomRepellent plugin;
    private final Map<UUID, RepellentEffect> activePlayers = new HashMap<>();
    private final NamespacedKey key;

    public EffectManager(PhantomRepellent plugin) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "phantom-repellent-duration");
    }

    @SuppressWarnings("ConstantConditions")
    public int repellentDuration(PersistentDataHolder holder) {
        PersistentDataContainer c = holder.getPersistentDataContainer();
        if (!c.has(key, PersistentDataType.INTEGER)) return -1;
        return c.get(key, PersistentDataType.INTEGER);
    }

    public void removeAll(UUID uuid) {
        RepellentEffect effect = activePlayers.getOrDefault(uuid, null);
        if (effect == null) return;
        effect.cancel();
        activePlayers.remove(uuid);
    }

    public void give(UUID uuid, int duration) {
        RepellentEffect effect = new RepellentEffect(this, duration, uuid);
        activePlayers.put(uuid, effect);
        effect.runTaskTimer(plugin, 0, 10);
    }

    @EventHandler
    public void on(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() != Material.POTION) return;

        ItemStack item = e.getItem();
        if (item.getItemMeta() == null) return;

        int duration = repellentDuration(item.getItemMeta());
        if (duration == -1) return;

        removeAll(e.getPlayer().getUniqueId());
        give(e.getPlayer().getUniqueId(), duration);
    }
}
