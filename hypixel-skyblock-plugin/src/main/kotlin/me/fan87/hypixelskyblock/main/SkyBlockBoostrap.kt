package me.fan87.hypixelskyblock.main

import me.fan87.hypixelskyblock.main.SkyBlock
import org.bukkit.plugin.java.JavaPlugin

class SkyBlockBoostrap: JavaPlugin() {

    override fun onDisable() {
        SkyBlock.onDisabled()
    }

    override fun onEnable() {
        SkyBlock.onEnable()
    }
}