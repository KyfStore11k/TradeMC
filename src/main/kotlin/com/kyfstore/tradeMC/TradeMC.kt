package com.kyfstore.tradeMC

import com.kyfstore.tradeMC.config.TradeMCConfig
import com.kyfstore.tradeMC.listeners.PlayerJoinListener
import org.bukkit.plugin.java.JavaPlugin
import xyz.xenondevs.invui.InvUI

class TradeMC : JavaPlugin() {

    override fun onLoad()
    {
        this.saveDefaultConfig();

        // CommandAPI.onLoad(CommandAPIBukkitConfig(this).verboseOutput(true))

        //CommandAPICommand("ping")
        //    .executes(CommandExecutor { sender, _ ->
        //        sender.sendMessage("pong!")
        //    })
        //    .register()
    }

    override fun onEnable() {
        logger.info("Enabling TradeMC...")

        logger.info("Saving and Preloading Configurations...")
        saveDefaultConfig()
        TradeMCConfig(this)
        logger.info("Successfully Setup Configurations!")

        logger.info("Registering Commands...")

        // CommandAPI.onEnable()

        logger.info("Successfully Enabled TradeMC Commands!")

        logger.info("Registering Listeners...")

        server.pluginManager.registerEvents(PlayerJoinListener(this), this)

        InvUI.getInstance().setPlugin(this);

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

    override fun onDisable() {
        logger.info("Disabling TradeMC...")

        // CommandAPI.onDisable()

        logger.info("Successfully Disabled TradeMC!")
    }
}
