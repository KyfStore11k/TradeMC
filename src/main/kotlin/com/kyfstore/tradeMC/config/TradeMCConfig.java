package com.kyfstore.tradeMC.config;

import com.kyfstore.tradeMC.TradeMC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import net.milkbowl.vault.economy.Economy;

import java.util.List;

public class TradeMCConfig {
    private TradeMC globalPlugin;
    private FileConfiguration globalConfig;

    private static Economy econ = null;

    public TradeMCConfig(@NotNull TradeMC plugin)
    {
        this.globalPlugin = plugin;
        this.globalConfig = plugin.getConfig();
        this.onEnable();
    }

    private void onEnable()
    {
        if (!setupEconomy()) {
            globalPlugin.getLogger().severe("Vault not found! Disabling plugin...");
            globalPlugin.getServer().getPluginManager().disablePlugin(globalPlugin);
        }
        this.reloadConfig();
    }

    private boolean setupEconomy() {
        if (globalPlugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            globalPlugin.getLogger().severe("Vault plugin not found! Ensure Vault is installed.");
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = globalPlugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            globalPlugin.getLogger().severe("No economy provider found! Ensure an economy plugin like EssentialsX is installed.");
            return false;
        }

        econ = rsp.getProvider();
        globalPlugin.getLogger().info("Vault hooked successfully with economy provider: " + econ.getName());
        return econ != null;
    }

    public void setPlayerBalance(OfflinePlayer player, double amount) {
        if (econ != null) {
            double currentBalance = econ.getBalance(player);
            double difference = amount - currentBalance;

            if (difference > 0) {
                econ.depositPlayer(player, difference);
            } else if (difference < 0) {
                econ.withdrawPlayer(player, -difference);
            }
        }
    }

    public void reloadConfig()
    {
        globalPlugin.reloadConfig();
        this.globalConfig = globalPlugin.getConfig();
        checkPlayerJoinHistory();
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

    public void checkPlayerJoinHistory()
    {
        List<String> playersJoined = globalConfig.getStringList("player-data.players-joined");

        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (!playersJoined.contains(player.getName()))
            {
                playersJoined.add(player.getName());
                globalConfig.set("player-data.players-joined", playersJoined);
                setPlayerBalance(player, globalConfig.getInt("economy.default-balance"));
                globalPlugin.saveConfig();
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
