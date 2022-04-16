package me.fan87.hypixelskyblock.configs

import me.fan87.hypixelskyblock.main.SkyBlock
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.lang.reflect.Field
import java.lang.reflect.Modifier

object Config {
    val config = YamlConfiguration()


    object LogLevel {
        var debug = true
        var info = true
        var warning = true
        var error = true
    }


    fun getConfigFile(): File {
        val file = File(SkyBlock.plugin.dataFolder, "config.yml")
        if (!file.parentFile.exists()) file.parentFile.mkdirs()
        if (!file.exists()) file.createNewFile()
        return file
    }

    fun saveConfig() {
        visitConfigValues("", javaClass) { prefix, field ->
            field.isAccessible = true
            config[prefix + field.name] = field[Config]
        }
        config.save(getConfigFile())
    }

    fun loadConfig() {
        config.load(getConfigFile())
        visitConfigValues("", javaClass) { prefix, field ->
            field.isAccessible = true
            if (config[prefix + field.name] != null) {
                field[Config] = config[prefix + field.name]
            }
        }
    }

    private fun visitConfigValues(prefix: String, outerClass: Class<*>, consumer: (prefix: String, field: Field) -> Unit) {
        for (field in outerClass.declaredFields) {
            if (
                Modifier.isPrivate(field.modifiers) &&
                Modifier.isPrivate(field.modifiers) &&
                !Modifier.isFinal(field.modifiers)
            ) {
                consumer(prefix, field)
            }
        }
        for (declaredClass in outerClass.classes) {
            visitConfigValues(prefix + declaredClass.simpleName + ".", declaredClass, consumer)
        }
    }

}