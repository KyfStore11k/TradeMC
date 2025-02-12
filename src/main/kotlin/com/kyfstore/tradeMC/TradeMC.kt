package com.kyfstore.tradeMC

import com.kyfstore.tradeMC.config.TradeMCConfig
// import com.kyfstore.tradeMC.commands.OUTDATED.commandTemplates.*
import com.kyfstore.tradeMC.commands.revamp.*
import com.kyfstore.tradeMC.listeners.PlayerJoinListener
import org.bukkit.plugin.java.JavaPlugin

class TradeMC : JavaPlugin() {

    // private lateinit var commodoreHandler: CommodoreHandler

    override fun onEnable() {
        logger.info("Enabling TradeMC...")

        logger.info("Saving and Preloading Configurations...")
        saveDefaultConfig()
        TradeMCConfig(this)
        logger.info("Successfully Setup Configurations!")

        logger.info("Registering Commands...")

        // this.commodoreHandler = CommodoreHandler(this)
        // TradeMCCommand(this)
        // TMCSellCommand(this)

        logger.info("Successfully Enabled TradeMC Commands!")

        logger.info("Registering Listeners...")

        server.pluginManager.registerEvents(PlayerJoinListener(this), this)

        logger.info("Successfully Enabled All Listeners")

        logger.info("Successfully Enabled TradeMC!")

        var firstTimeUse = config.getBoolean("settings.general.first-time-use")

        if (firstTimeUse)
        {
            logger.warning("It is your first time using TradeMC in this server!")
            logger.warning("It is recommended to run /trademc reload ")
            config.set("settings.general.first-time-use", false);
            saveConfig();
        }
    }

    // fun commodoreHandler(): CommodoreHandler = commodoreHandler

    override fun onDisable() {
        logger.info("Disabling TradeMC...")

        logger.info("Successfully Disabled TradeMC!")
    }
}
