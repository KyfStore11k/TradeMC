package com.kyfstore.tradeMC.listeners;

import com.kyfstore.tradeMC.config.TradeMCConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        TradeMCConfig.tradeMCConfig.checkPlayerJoinHistory(event.getPlayer());
    }
}
