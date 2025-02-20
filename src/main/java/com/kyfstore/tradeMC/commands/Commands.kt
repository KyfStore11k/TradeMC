package com.kyfstore.tradeMC.commands

import com.kyfstore.tradeMC.config.TradeMCConfig
import dev.jorel.commandapi.CommandAPIBukkitConfig
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.ItemStackArgument
import dev.jorel.commandapi.arguments.LongArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandExecutor
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

@SuppressWarnings("LongMethod")
object Commands {
    fun register(plugin: JavaPlugin) {
        CommandAPICommand("trademc")
            .withSubcommand(
                CommandAPICommand("help")
                    .withPermission("trademc.help")
                    .executes(CommandExecutor { sender, _ ->
                        sender.sendMessage(miniMessage().deserialize("<green>=== TradeMC Help ==="))
                        sender.sendMessage(miniMessage().deserialize("<yellow>/trademc help <green>|<white> Shows this help screen"))
                        sender.sendMessage(miniMessage().deserialize("<yellow>/trademc version <green>|<white> Shows the version of TradeMC"))
                        sender.sendMessage(miniMessage().deserialize("<yellow>/trademc reload <green>|<white> Reloads TradeMC"))
                        sender.sendMessage(miniMessage().deserialize("<yellow>/trademc config <set|show> <valuePath> <value> <green>|<white> Set's the config for TradeMC"))
                    })
            )
            .withSubcommand(
                CommandAPICommand("version")
                    .withPermission("trademc.version")
                    .executes(CommandExecutor { sender, _ ->
                        sender.sendMessage(miniMessage().deserialize("<yellow>This server is running TradeMC <green>" + plugin.pluginMeta.version))
                    })
            )
            .withSubcommand(
                CommandAPICommand("reload")
                    .withPermission("trademc.admin.reload")
                    .executes(CommandExecutor { sender, _ ->
                        sender.sendMessage(miniMessage().deserialize("<yellow>Reloading TradeMC..."))
                        TradeMCConfig.tradeMCConfig.reloadConfig()
                        sender.sendMessage(miniMessage().deserialize("<green>TradeMC Reloaded!"))
                    })
            )
            .withSubcommand(
                CommandAPICommand("config")
                    .withSubcommand(
                        CommandAPICommand("set")
                            .withArguments(StringArgument("valuePath"), StringArgument("value"))
                            .withPermission("trademc.admin.config.set")
                            .executes(CommandExecutor {sender, args ->
                                val valuePath: String = args.get("valuePath").toString()
                                val value: String = args.get("value").toString()


                                TradeMCConfig.tradeMCConfig.setValue(valuePath, value)
                                sender.sendMessage(miniMessage().deserialize("<green>TradeMC Config path: $valuePath was set to: $value"))
                            })
                    )
                    .withSubcommand(
                        CommandAPICommand("show")
                            .withPermission("trademc.admin.config.show")
                            .executes(CommandExecutor { sender, _ ->
                                val list: List<String> = TradeMCConfig.tradeMCConfig.showValues() as List<String>
                                sender.sendMessage(miniMessage().deserialize("<yellow>=== TradeMC config settings==="))
                                for (key: String in list) {
                                    val value: Any? = TradeMCConfig.tradeMCConfig.globalConfig.get(key)
                                    val fullPath: String = if ("".isEmpty()) key else ".$key"
                                    if (value !is ConfigurationSection) {
                                        sender.sendMessage(miniMessage().deserialize("<green>$fullPath: <yellow>$value"))
                                    }
                                }
                            })
                    )
            )
            .register()

        CommandAPICommand("sell")
            .withArguments(ItemStackArgument("item"), LongArgument("count"), LongArgument("price"))
            .withPermission("trademc.sell")
            .executes(CommandExecutor {sender, args ->
                if (sender is Player) {
                    val items: ArrayList<ItemStack> = ArrayList()
                    var itemCount: Long = args.get("count") as Long
                    var playerItemCount = 0L

                    for (item: ItemStack? in sender.inventory.contents) {
                        if ((item != null) && (item.type == (args.get("item") as ItemStack).type)) {
                            items.add(item)
                            playerItemCount += item.amount.toLong()
                        }
                    }

                    if (playerItemCount < itemCount) {
                        sender.sendMessage(
                            miniMessage().deserialize(
                                "<red>You don't have enough " +
                                miniMessage().serialize(Component.translatable((args.get("item") as ItemStack).translationKey())) +
                                "(s) to sell!")
                        )
                        return@CommandExecutor
                    }

                    @SuppressWarnings("LoopWithTooManyJumpStatements")
                    for (item in items) {
                        val count = item.amount

                        if (count > itemCount) {
                            item.amount = (count - itemCount).toInt()
                            break
                        } else if (itemCount > 0) {
                            sender.inventory.removeItem(item)
                            itemCount -= count
                        } else {
                            break
                        }
                    }
                } else {
                    sender.sendMessage(miniMessage().deserialize("<red>The server can't run /sell"))
                    return@CommandExecutor
                }
            }).register()
    }

    fun getCommandAPIConfig(plugin: JavaPlugin): CommandAPIBukkitConfig {
        val commandAPIConfig = CommandAPIBukkitConfig(plugin)

        commandAPIConfig.silentLogs(!plugin.config.getBoolean("settings.general.verbose"))
        commandAPIConfig.verboseOutput(plugin.config.getBoolean("settings.general.verbose"))
        commandAPIConfig.useLatestNMSVersion(false)
        commandAPIConfig.beLenientForMinorVersions(true)
        commandAPIConfig.missingExecutorImplementationMessage("No executor Impl? Weird? (Create a github issue)")
        commandAPIConfig.shouldHookPaperReload(true)
        commandAPIConfig.skipReloadDatapacks(false)
        commandAPIConfig.usePluginNamespace()
        return commandAPIConfig
    }
}
