package com.kyfstore.tradeMC.economy

import com.kyfstore.tradeMC.TradeMC
import net.milkbowl.vault.economy.Economy
import org.bukkit.OfflinePlayer

class TradeMCEconomy(private val globalPlugin: TradeMC) {

    init {
        tradeMCEconomy = this
    }

    fun setupEconomy() {
        if (!globalPlugin.server.pluginManager.isPluginEnabled("Vault")) {
            globalPlugin.componentLogger.warn("Vault not found. Not setting up economy features.")
        } else {
            val rsp = globalPlugin.server.servicesManager.getRegistration(Economy::class.java)
            if (rsp == null) {
                globalPlugin.componentLogger.warn("No economy provider found. Not setting up economy features.")
                return
            }

            econ = rsp.provider
            isEco = true
            globalPlugin.logger.info("Vault hooked successfully with economy provider: " + econ.name)
        }
    }

    fun setPlayerBalance(player: OfflinePlayer, amount: Double) {
        if (isEco) {
            val currentBalance = econ.getBalance(player)
            val difference = amount - currentBalance

            if (difference > 0) {
                econ.depositPlayer(player, difference)
            } else if (difference < 0) {
                econ.withdrawPlayer(player, -difference)
            }
        }
    }

    companion object {
        lateinit var tradeMCEconomy: TradeMCEconomy
        lateinit var econ: Economy
        var isEco = false
    }
}
