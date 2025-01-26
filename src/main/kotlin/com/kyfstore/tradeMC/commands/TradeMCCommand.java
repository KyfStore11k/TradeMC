package com.kyfstore.tradeMC.commands;

import com.kyfstore.tradeMC.TradeMC;
import com.kyfstore.tradeMC.commands.commandTemplates.ParaCommand;
import com.kyfstore.tradeMC.config.TradeMCConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TradeMCCommand extends ParaCommand {
    private final TradeMC _core;

    public TradeMCCommand(@NotNull TradeMC core)
    {
        super(core, "trademc");
        this._core = core;
    }

    @Override
    public void run(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args)
    {
        Player player = null;
        if (sender instanceof Player) player = (Player) sender;
        final boolean isPlayer = (player != null);

        if (args.length < 1)
        {
            if (isPlayer) player.sendMessage("Usage: /trademc <reload|config|version|help>");
            else _core.getLogger().info("Usage: `/trademc <reload|config|version|help>`");
            return;
        }

        if (args[0].equalsIgnoreCase("help")) {
            if (args.length > 1)
            {
                if (isPlayer) player.sendMessage("Usage: /trademc help");
                else _core.getLogger().info("Usage: `/trademc help`");
                return;
            }
            if (isPlayer) {
                player.sendMessage(ChatColor.GREEN + "=== TradeMC Help ===");
                player.sendMessage(ChatColor.YELLOW + "/trademc help" + ChatColor.GREEN + " | " + ChatColor.WHITE + "Shows this help screen");
                player.sendMessage(ChatColor.YELLOW + "/trademc version" + ChatColor.GREEN + " | " + ChatColor.WHITE + "Shows the version of TradeMC");
                player.sendMessage(ChatColor.YELLOW + "/trademc reload" + ChatColor.GREEN + " | " + ChatColor.WHITE + "Shows this help screen");
                player.sendMessage(ChatColor.YELLOW + "/trademc config <set|show> <valuePath> <value>" + ChatColor.GREEN + " | " + ChatColor.WHITE + "Customizes TradeMC's Settings");
            } else {
                _core.getLogger().warning("=== TradeMC Help ===");
                _core.getLogger().warning("/trademc help" + " | " + "Shows this help screen");
                _core.getLogger().warning("/trademc version" + " | " + "Shows the version of TradeMC");
                _core.getLogger().warning("/trademc reload" + " | " + "Shows this help screen");
                _core.getLogger().warning("/trademc config <set|show> <valuePath> <value>" + " | " + "Customizes TradeMC's Settings");
            }
            return;
        }

        if (args[0].equalsIgnoreCase("version")) {
            if (args.length > 1)
            {
                if (isPlayer) player.sendMessage("Usage: /trademc version");
                else _core.getLogger().info("Usage: `/trademc version`");
                return;
            }
            if (isPlayer) player.sendMessage(ChatColor.YELLOW + "This Server is Currently Using TradeMC Version: " + _core.getDescription().getVersion());
            else _core.getLogger().warning("TradeMC Version: " + _core.getDescription().getVersion());
            return;
        }
        TradeMCConfig config = new TradeMCConfig(_core);

        if (args[0].equalsIgnoreCase("reload")) {
            if (args.length > 1)
            {
                if (isPlayer) player.sendMessage("Usage: /trademc reload");
                else _core.getLogger().info("Usage: `/trademc reload`");
                return;
            }

            if (isPlayer) {
                player.sendMessage("Reloading TradeMC...");
                config.reloadConfig();
                player.sendMessage("Successfully Reloaded TradeMC!");
            } else {
                _core.getLogger().warning("Reloading TradeMC...");
                config.reloadConfig();
                _core.getLogger().warning("Successfully Reloaded TradeMC!");
            }
            return;
        }

        if (args[0].equalsIgnoreCase("config")) {
            try {
                if (args[1].equalsIgnoreCase("show")) {
                    if (args.length > 2) {
                        if (isPlayer) player.sendMessage("Usage: /trademc config <set|show> <valuePath> <value>");
                        else _core.getLogger().warning("Usage: `/trademc config <set|show> <valuePath> <value>`");
                        return;
                    }
                    config.showValues(sender);
                } else if (args[1].equalsIgnoreCase("set")) {
                    if (args.length > 4) {
                        if (isPlayer) player.sendMessage("Usage: /trademc config <set|show> <valuePath> <value>");
                        else _core.getLogger().warning("Usage: `/trademc config <set|show> <valuePath> <value>`");
                        return;
                    } else if (args.length < 4) {
                        if (isPlayer) player.sendMessage("Usage: /trademc config <set|show> <valuePath> <value>");
                        else _core.getLogger().warning("Usage: `/trademc config <set|show> <valuePath> <value>`");
                        return;
                    }

                    config.setValue(args[2], args[3], isPlayer, player);
                }
            } catch (Exception e) {
                if (isPlayer) player.sendMessage("Usage: /trademc config <set|show> <valuePath> <value>");
                else _core.getLogger().warning("Usage: `/trademc config <set|show> <valuePath> <value>`");
                return;
            }
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) return null;

        if (args.length == 3) {
            if (args[1].equalsIgnoreCase("set"))
            {
                List<String> configPaths = getAllConfigPaths(_core.getConfig(), "");
                return configPaths;
            }
        }

        return null;
    }

    private List<String> getAllConfigPaths(ConfigurationSection section, String prefix) {
        List<String> paths = new ArrayList<>();

        for (String key : section.getKeys(false)) {
            String fullPath = prefix.isEmpty() ? key : prefix + "." + key;

            Object value = section.get(key);

            if (value instanceof ConfigurationSection) {
                paths.addAll(getAllConfigPaths((ConfigurationSection) value, fullPath));
            } else {
                paths.add(fullPath);
            }
        }

        return paths;
    }
}
