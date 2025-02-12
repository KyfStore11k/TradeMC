package com.kyfstore.tradeMC.listeners;

import com.kyfstore.tradeMC.TradeMC;
import com.kyfstore.tradeMC.config.TradeMCConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerJoinListener implements Listener {

    private final JavaPlugin plugin;

    public PlayerJoinListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        TradeMCConfig tradeMCConfig = new TradeMCConfig((TradeMC) plugin);
        tradeMCConfig.reloadConfig();
    }
}
