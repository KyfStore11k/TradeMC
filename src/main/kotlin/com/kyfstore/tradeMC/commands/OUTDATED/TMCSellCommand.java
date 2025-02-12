package com.kyfstore.tradeMC.commands.OUTDATED;

import com.kyfstore.tradeMC.TradeMC;
import com.kyfstore.tradeMC.commands.OUTDATED.commandTemplates.ParaCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.*;

import org.bukkit.inventory.*;

import java.util.*;
import java.util.stream.*;

public class TMCSellCommand extends ParaCommand {
    private final TradeMC _core;

    public TMCSellCommand(@NotNull TradeMC core)
    {
        super(core, "tmc_sell");
        this._core = core;
    }
    public boolean removeItems(Player player, Material material, int amount) {
        Inventory inventory = player.getInventory();
        int totalCount = 0;

        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == material) {
                totalCount += item.getAmount();
            }
        }

        if (totalCount < amount) {
            player.sendMessage(ChatColor.RED + "You don't have enough of that item to sell!");
            return false;
        }

        int remaining = amount;
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == material) {
                int count = item.getAmount();

                if (count > remaining) {
                    item.setAmount(count - remaining);
                    break;
                } else {
                    inventory.removeItem(item);
                    remaining -= count;
                }

                if (remaining <= 0) {
                    break;
                }
            }
        }
        return true;
    }
    public Material getMaterialFromString(String name) {
        try {
            return Material.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public void run(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player player = null;
        if (sender instanceof Player) player = (Player) sender;
        final boolean isPlayer = (player != null);

        if (!isPlayer)
        {
            _core.getLogger().warning("Server cannot execute command: `/tmc_sell`");
            return;
        }

        if (args.length != 3) {
            if (isPlayer) player.sendMessage("Usage: /tmc_sell <minecraft_item> <item_count> <sell_price>");
            else _core.getLogger().warning("Usage: `/tmc_sell <minecraft_item> <item_count> <sell_price>`");
            return;
        }

        final String minecraftItem = args[0];
        final long itemCount = Long.parseLong(args[1]);
        final long sellPrice = Long.parseLong(args[2]);

        boolean success = removeItems(player, getMaterialFromString(minecraftItem), (int) itemCount);

        if (success)
        {
            player.sendMessage(ChatColor.GREEN + "Successfully sold: \"" + minecraftItem + "\" for " + sellPrice * itemCount + " " + _core.getConfig().getString("settings.general.currency_name") + "!");
        }

        // Put items up for sale on the shop for the `sellPrice` amount!
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1)
        {
            return Arrays.asList(getAllItemNames());
        } else if (args.length == 2) {
            return IntStream.rangeClosed(1, 200)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.toList());
        } else if (args.length == 3) {
            return IntStream.rangeClosed(1, 500)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.toList());
        }

        return null;
    }

    public String[] getAllItemNames() {
        return Arrays.stream(Material.values())
                .map(material -> material.name().toLowerCase())
                .toArray(String[]::new);
    }
}
