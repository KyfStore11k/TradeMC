package com.kyfstore.tradeMC.commands.OUTDATED.commandTemplates;

import com.kyfstore.tradeMC.TradeMC;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import me.lucko.commodore.file.CommodoreFileReader;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class CommodoreHandler {

    private final TradeMC core;
    private Commodore commodore = null;

    public CommodoreHandler(@NotNull final TradeMC core)
    {
        this.core = core;
        if (CommodoreProvider.isSupported()) this.commodore = CommodoreProvider.getCommodore(core);
    }

    public void register(@NotNull final PluginCommand command, @NotNull String nodeFileName) {
        if (commodore == null) return;
        if (!nodeFileName.endsWith(".commodore")) nodeFileName += ".commodore";

        final LiteralCommandNode<?> node;
        try { node = CommodoreFileReader.INSTANCE.parse(core.getResource(nodeFileName)); }
        catch (IOException | RuntimeException e) {
            e.printStackTrace();
            return;
        }

        commodore.register(command, node);
    }

}
