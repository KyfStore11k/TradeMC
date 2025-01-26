package com.kyfstore.tradeMC

import com.kyfstore.tradeMC.config.TradeMCConfig
import com.kyfstore.tradeMC.commands.*
import com.kyfstore.tradeMC.commands.commandTemplates.*
import org.bukkit.plugin.java.JavaPlugin

class TradeMC : JavaPlugin() {

    private lateinit var commodoreHandler: CommodoreHandler

    override fun onEnable() {
        logger.info("Enabling TradeMC...")

        logger.info("Saving and Preloading Configurations...")
        saveDefaultConfig()
        TradeMCConfig(this)
        logger.info("Successfully Setup Configurations!")

        logger.info("Registering Commands...")

        this.commodoreHandler = CommodoreHandler(this)
        TradeMCCommand(this)

        logger.info("Successfully Enabled Brigadier and TradeMC Commands!")

        logger.info("Successfully Enabled TradeMC!")
    }

    fun commodoreHandler(): CommodoreHandler = commodoreHandler

    override fun onDisable() {
        logger.info("Disabling TradeMC...")

        logger.info("Successfully Disabled TradeMC!")
    }
}
