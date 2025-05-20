package com.example.levelplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LevelPlugin extends JavaPlugin implements Listener {
    private Map<UUID, Integer> playerExp = new HashMap<>();
    private FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("levelmenu").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player) {
                openLevelMenu((Player) sender);
            }
            return true;
        });
    }

    public void addExp(Player player, int amount) {
        UUID uuid = player.getUniqueId();
        int currentExp = playerExp.getOrDefault(uuid, 0);
        playerExp.put(uuid, currentExp + amount);
    }

    private int getLevel(Player player) {
        int exp = playerExp.getOrDefault(player.getUniqueId(), 0);
        int level = 1;
        for (String key : config.getConfigurationSection("levels").getKeys(false)) {
            int requiredExp = config.getInt("levels." + key);
            if (exp >= requiredExp) {
                level = Integer.parseInt(key);
            }
        }
        return level;
    }

    private void openLevelMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "Level Progression");
        int playerLevel = getLevel(player);
        for (int i = 1; i <= 5; i++) {
            Material material = (i <= playerLevel) ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE;
            ItemStack pane = new ItemStack(material);
            ItemMeta meta = pane.getItemMeta();
            meta.setDisplayName("Level " + i);
            pane.setItemMeta(meta);
            inv.setItem(i - 1, pane);
        }
        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Level Progression")) {
            event.setCancelled(true);
        }
    }
}