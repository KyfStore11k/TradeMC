package com.kyfstore.tradeMC.config

import com.kyfstore.tradeMC.TradeMC
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import java.util.*

class TradeMCConfig(private val globalPlugin: TradeMC) {
    var globalConfig: FileConfiguration

    init {
        tradeMCConfig = this
        this.globalConfig = globalPlugin.config
    }

    //public void onEnable() {
    //}
    fun reloadConfig() {
        globalPlugin.saveDefaultConfig()
        globalPlugin.reloadConfig()
        this.globalConfig = globalPlugin.config
    }

    fun setValue(valuePath: String, value: Any?) {
        if (globalConfig.contains(valuePath)) {
            if (value.toString().toBooleanStrictOrNull() != null) {
                globalConfig[valuePath] = value.toString().toBooleanStrict()
            } else {
                globalConfig[valuePath] = value
            }
            globalPlugin.saveConfig()
        }
    }

    fun checkPlayerJoinHistory() {
        val playersJoined = globalConfig.getStringList("player-data.players-joined")

        for (player in Bukkit.getOnlinePlayers()) {
            if (!playersJoined.contains(player.uniqueId.toString())) {
                playersJoined.add(player.uniqueId.toString())
                globalConfig["player-data.players-joined"] = playersJoined
                globalPlugin.saveConfig()
            }
        }
    }

    fun checkPlayerJoinHistory(plr: Player) {
        val playersJoined = globalConfig.getStringList("player-data.players-joined")

        if (!playersJoined.contains(plr.uniqueId.toString())) {
            playersJoined.add(plr.uniqueId.toString())
            globalConfig["player-data.players-joined"] = playersJoined
            globalPlugin.saveConfig()
        }
    }

    fun showValues(): List<Any> {
        return traverseConfig(globalConfig)
    }

    private fun traverseConfig(section: ConfigurationSection): List<Any> {
        val list: MutableList<Any> = ArrayList(listOf())
        list.addAll(section.getKeys(true))
        return list
    }

    companion object {
        lateinit var tradeMCConfig: TradeMCConfig
    }
}
