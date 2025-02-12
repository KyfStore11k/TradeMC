package com.kyfstore.tradeMC.commands.OUTDATED.commandTemplates;

import com.kyfstore.tradeMC.TradeMC;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ParaCommand implements CommandExecutor, TabCompleter {
    public ParaCommand(@NotNull final TradeMC core, @NotNull final String name) {
        final PluginCommand command = core.getCommand(name);
        if (command == null) return;

        command.setExecutor(this);
        command.setTabCompleter(this);
        // core.commodoreHandler().register(command, name);
    }

    public abstract void run(
        @NotNull CommandSender sender,
        @NotNull String label,
        @NotNull String[] args
    );

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        run(commandSender, s, strings);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        return null;
    }
}
