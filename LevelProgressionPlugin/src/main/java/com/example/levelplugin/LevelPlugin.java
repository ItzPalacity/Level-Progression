package com.levelplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class LevelPlugin extends JavaPlugin implements Listener {

    private static LevelPlugin instance;
    private LevelManager levelManager;
    private GUIManager guiManager;
    private ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.levelManager = new LevelManager(this);
        this.guiManager = new GUIManager(this);
        this.scoreboardManager = new ScoreboardManager(this);

        getCommand("levelmenu").setExecutor((TabExecutor) new LevelMenuCommand(this));
        Bukkit.getPluginManager().registerEvents(guiManager, this);
        Bukkit.getPluginManager().registerEvents(this, this);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                scoreboardManager.updateScoreboard(player);
            }
        }, 0L, 40L);
    }

    public static LevelPlugin getInstance() {
        return instance;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public GUIManager getGUIManager() {
        return guiManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            guiManager.openLevelGUI(player, 0);
        }
        return true;
    }
}
