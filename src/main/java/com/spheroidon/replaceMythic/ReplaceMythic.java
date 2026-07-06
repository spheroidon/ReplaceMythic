package com.spheroidon.replaceMythic;

import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class ReplaceMythic extends JavaPlugin implements Listener {

    private boolean debug;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("ReplaceMythic is enabled!");

        debug = getConfig().getBoolean("Debug");
        if (debug) {
            getLogger().info("ReplaceMythic debug enabled!");
        }
    }

    @Override
    public void onDisable() {
       getLogger().info("Disabling ReplaceMythic...");
    }

    public ItemStack getReplacingItem(ItemStack item) {
        if(debug) {
            getLogger().info("Replacing item: " + item.toString());
        }
        FileConfiguration config = getConfig();
        if(item == null) {
            return null;
        }
        String resultItemID = config.getString("ReplaceItems."+item.getType()+".MythicID");
        if(resultItemID == null) {
            if(debug) {
                getLogger().warning("Item should not be replaced!");
            }
            return item;
        }
        ItemStack resultItem = MythicBukkit.inst().getItemManager().getItemStack(resultItemID, item.getAmount());
        if(resultItem == null) {
            getLogger().warning("Could not find Mythic item with ID "+resultItemID+". Removing item instead.");
            return item;
        } else {
            if(debug) {
                getLogger().info("Replaced item: " + resultItem);
            }
            return resultItem;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(debug) {
            getLogger().info("Replacing item from InventoryClickEvent!");
        }
        ItemStack newItem = getReplacingItem(event.getCurrentItem());
        if(newItem != null) {
            event.setCurrentItem(newItem);
        } else {
            if(debug) {
                getLogger().warning("Could not replace item from InventoryClickEvent: item is null.");
            }
        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if(event.getEntity().getType() != EntityType.PLAYER) return;
        if(debug) {
            getLogger().info("Replacing item from EntityPickupItemEvent!");
        }
        ItemStack newItem = getReplacingItem(event.getItem().getItemStack());
        if(newItem != null) {
            event.getItem().setItemStack(newItem);
        } else {
            if(debug) {
                getLogger().warning("Could not replace item from EntityPickupItemEvent: item is null.");
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if(debug) {
            getLogger().info("Replacing item from PlayerDropItemEvent!");
        }
        ItemStack newItem = getReplacingItem(event.getItemDrop().getItemStack());
        if(newItem != null) {
            event.getItemDrop().setItemStack(newItem);
        } else {
            if(debug) {
                getLogger().warning("Could not replace item from PlayerDropItemEvent: item is null.");
            }
        }
    }
}
