package me.fan87.hypixelskyblock.utils

import me.fan87.hypixelskyblock.configs.Config
import org.bukkit.Bukkit
import org.bukkit.ChatColor

object Logger {

    private val prefix = "${ChatColor.BLUE}[Hypixel SkyBlock] "

    fun debug(message: Any) {
        if (Config.LogLevel.debug) {
            Bukkit.getConsoleSender().sendMessage("$prefix $message")
        }
    }

    fun info(message: Any) {
        if (Config.LogLevel.info) {
            Bukkit.getConsoleSender().sendMessage("$prefix $message")
        }
    }

    fun error(message: Any, throwable: Throwable) {
        if (Config.LogLevel.error) {
            Bukkit.getConsoleSender().sendMessage("$prefix $message")
            throwable.printStackTrace()
        }
    }

    fun warning(message: Any) {
        if (Config.LogLevel.warning) {
            Bukkit.getConsoleSender().sendMessage("$prefix $message")
        }
    }

}