package com.kyfstore.tradeMC.config;

import com.kyfstore.tradeMC.TradeMC;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TradeMCConfig {
    private TradeMC globalPlugin;
    private FileConfiguration globalConfig;
    public TradeMCConfig(@NotNull TradeMC plugin)
    {
        this.globalPlugin = plugin;
        this.globalConfig = plugin.getConfig();
        this.onEnable();
    }

    private void onEnable()
    {
        this.reloadConfig();
    }

    public void reloadConfig()
    {
        globalPlugin.reloadConfig();
        this.globalConfig = globalPlugin.getConfig();
    }
    public void setValue(String valuePath, Object value, boolean isPlayer, Player player)
    {
        if (isPlayer) {
            if (globalConfig.contains(valuePath)) {
                globalConfig.set(valuePath, value);
                globalPlugin.saveConfig();
                player.sendMessage(ChatColor.GREEN + "TradeMC Config Path: " + valuePath + "; was set to the value: " + value);
                player.sendMessage(ChatColor.GREEN + "It is recommended to reload TradeMC with: /trademc reload");
            }
        } else {
            if (globalConfig.contains(valuePath)) {
                globalConfig.set(valuePath, value);
                globalPlugin.saveConfig();
                globalPlugin.getLogger().warning("TradeMC Config Path: " + valuePath + "; was set to the value: " + value);
                globalPlugin.getLogger().warning("It is recommended to reload TradeMC with: `/trademc reload`");
            }
        }
    }

    public void showValues(CommandSender sender)
    {
        Player player = null;
        if (sender instanceof Player) player = (Player) sender;
        final boolean isPlayer = (player != null);

        if (isPlayer)
        {
            player.sendMessage(ChatColor.GREEN + "=== TradeMC Config Settings ===");
            traverseConfig("", globalConfig, sender, isPlayer);
        } else {
            globalPlugin.getLogger().warning("=== TradeMC Config Settings ===");
            traverseConfig("", globalConfig, sender, isPlayer);
        }
    }

    private void traverseConfig(String prefix, ConfigurationSection section, CommandSender sender, boolean isPlayer) {
        if (isPlayer) {
            for (String key : section.getKeys(false)) {
                Object value = section.get(key);

                String fullPath = prefix.isEmpty() ? key : prefix + "." + key;

                if (value instanceof ConfigurationSection) {
                    sender.sendMessage(ChatColor.GOLD + fullPath + ":");
                    traverseConfig(fullPath, (ConfigurationSection) value, sender, isPlayer);
                } else if (value instanceof List) {
                    sender.sendMessage(ChatColor.GREEN + fullPath + ": " + ChatColor.YELLOW + value.toString());
                } else {
                    sender.sendMessage(ChatColor.GREEN + fullPath + ": " + ChatColor.YELLOW + value);
                }
            }
        } else {
            for (String key : section.getKeys(false)) {
                Object value = section.get(key);

                String fullPath = prefix.isEmpty() ? key : prefix + "." + key;

                if (value instanceof ConfigurationSection) {
                    globalPlugin.getLogger().warning(fullPath + ":");
                    traverseConfig(fullPath, (ConfigurationSection) value, sender, isPlayer);
                } else if (value instanceof List) {
                    globalPlugin.getLogger().warning(fullPath + ": " + value.toString());
                } else {
                    globalPlugin.getLogger().warning(fullPath + ": " + value);
                }
            }
        }
    }

}
