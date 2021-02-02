package me.lucyy.phantomrepellent;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class EffectManager implements Listener {
    private final PhantomRepellent plugin;
    private final Map<UUID, RepellentEffect> activePlayers = new HashMap<>();
    private final NamespacedKey key;
    private final NamespacedKey catKey;

    public EffectManager(PhantomRepellent plugin) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "phantom-repellent-duration");
        this.catKey = new NamespacedKey(plugin, "is-phantom-repellent");
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
        activePlayers.remove(uuid);
        effect.cancel();
    }

    public void give(UUID uuid, int duration) {
        RepellentEffect effect = new RepellentEffect(this, duration, uuid, catKey);
        activePlayers.put(uuid, effect);
        effect.runTaskTimer(plugin, 0, 20);
    }

    @SuppressWarnings("ConstantConditions")
    public ItemStack getPotion(int duration) {
        ItemStack item = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) item.getItemMeta();

        // TODO config values for these params
        meta.setColor(Color.fromRGB(255, 216, 41));
        meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Ｐｈａｎｔｏｍ Ｒｅｐｅｌｌｅｎｔ Ｐｏｔｉｏｎ");
        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, duration);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ENCHANTS);
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.setLore(Collections.singletonList(ChatColor.BLUE + "Phantom Repellent (" +
				PhantomRepellent.formatTime(duration) + ")"));

        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void on(PlayerItemConsumeEvent e) {
    	if (e.getItem().getType() == Material.MILK_BUCKET) {
    		removeAll(e.getPlayer().getUniqueId());
    		return;
		}
        if (e.getItem().getType() != Material.POTION) return;

        ItemStack item = e.getItem();
        if (item.getItemMeta() == null) return;

        int duration = repellentDuration(item.getItemMeta());
        if (duration == -1) return;

        removeAll(e.getPlayer().getUniqueId());
        give(e.getPlayer().getUniqueId(), duration);
    }

    @EventHandler
    @SuppressWarnings("ConstantConditions")
    public void on(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        if (pdc.has(key, PersistentDataType.INTEGER)) {
            give(player.getUniqueId(), pdc.get(key, PersistentDataType.INTEGER));
            pdc.remove(key);
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (activePlayers.containsKey(player.getUniqueId())) {
            player.getPersistentDataContainer().set(key, PersistentDataType.INTEGER,
                    activePlayers.get(player.getUniqueId()).getLeftDuration());
            removeAll(player.getUniqueId());
        }
    }
}
