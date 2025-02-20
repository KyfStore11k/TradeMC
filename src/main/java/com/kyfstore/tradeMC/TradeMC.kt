package com.kyfstore.tradeMC

import com.kyfstore.tradeMC.commands.Commands
import com.kyfstore.tradeMC.config.TradeMCConfig
import com.kyfstore.tradeMC.economy.TradeMCEconomy
import com.kyfstore.tradeMC.listeners.PlayerJoinListener
import org.bukkit.plugin.java.JavaPlugin
import xyz.xenondevs.invui.InvUI
import dev.jorel.commandapi.CommandAPI

class TradeMC: JavaPlugin() {
    override fun onLoad() {
        saveDefaultConfig()
        TradeMCConfig(this)

        CommandAPI.onLoad(Commands.getCommandAPIConfig(this))
        Commands.register(this)
    }

    override fun onEnable() {
        if (config.getBoolean("settings.general.verbose")) {
            logger.info("Saving and Preloading Configurations...")
            //saveDefaultConfig()
            //TradeMCConfig.tradeMCConfig.onEnable()
            TradeMCEconomy(this).setupEconomy()
            logger.info("Successfully Setup Configurations!")

            logger.info("Registering Commands...")

            CommandAPI.onEnable()

            logger.info("Successfully Enabled TradeMC Commands!")

            logger.info("Registering Listeners...")

            server.pluginManager.registerEvents(PlayerJoinListener(), this)

            InvUI.getInstance().setPlugin(this)

            logger.info("Successfully Enabled All Listeners")

            logger.info("Successfully Enabled TradeMC!")

            val firstLoad = config.getBoolean("settings.general.first-load")

            if (firstLoad) {
                config["settings.general.first-load"] = false
                saveConfig()
            }
        } else {
            TradeMCEconomy(this).setupEconomy()
            CommandAPI.onEnable()
            server.pluginManager.registerEvents(PlayerJoinListener(), this)
            InvUI.getInstance().setPlugin(this)
            val firstLoad = config.getBoolean("settings.general.first-load")
            if (firstLoad) {
                config["settings.general.first-load"] = false
                saveConfig()
            }
        }
    }

    override fun onDisable() {
        logger.info("Disabling TradeMC...")

        CommandAPI.onDisable()

        logger.info("Successfully Disabled TradeMC!")
    }
}
