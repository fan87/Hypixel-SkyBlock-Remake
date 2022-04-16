package me.fan87.hypixelskyblock.main

import me.fan87.hypixelskyblock.configs.Config
import me.fan87.hypixelskyblock.events.EventsManager
import me.fan87.hypixelskyblock.player.PlayersManager
import me.fan87.hypixelskyblock.utils.Logger
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

object SkyBlock {

    val plugin: Plugin = Bukkit.getPluginManager().getPlugin("Hypixel-SkyBlock-Remake")


    internal fun onEnable() {

        Logger.info("Initializing Configs Manager...")
        Config.loadConfig()
        Config.saveConfig()

        Logger.info("Initializing Events Manager...")
        EventsManager

        Logger.info("Initializing Players Manager...")
        PlayersManager


        Logger.info("Hypixel SKyBlock plugin has been enabled and loaded!")
    }

    internal fun onDisabled() {

    }



}