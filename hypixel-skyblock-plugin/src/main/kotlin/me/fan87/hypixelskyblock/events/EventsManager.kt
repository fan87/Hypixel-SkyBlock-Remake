package me.fan87.hypixelskyblock.events

import me.fan87.hypixelskyblock.main.SkyBlock
import me.fan87.hypixelskyblock.utils.Logger
import me.fan87.hypixelskyblock.utils.TopologicalGraph
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.reflections.Reflections
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

object EventsManager {

    private var latestEventHandlerId = Int.MIN_VALUE
    private var handlerList = ArrayList<EventHandler<*>>()

    private val handlerListLock = ReentrantLock()

    init {
        val reflections = Reflections(Event::class.java.`package`.name)

        for (clazz in reflections.getSubTypesOf(Event::class.java)) {
            if (clazz.declaredMethods.any { it.name == "getHandlers" }) {
                Bukkit.getPluginManager().registerEvent(clazz, object: Listener{}, EventPriority.NORMAL,
                    { _, event ->
                        callEvent(event)
                    }, SkyBlock.plugin)
            }
        }
    }

    fun callEvent(event: Any) {
        handlerListLock.withLock {

        }
    }

    fun registerListener(listener: Any) {
        handlerListLock.withLock {
            registerListener()
        }
    }

    fun <E> registerListener(handler: (E) -> Unit,
                             name: String = "",
                             mustRunBefore: Array<String> = Array(0) { "" },
                             vararg mustRunAfter: String) {
        handlerListLock.withLock {
            val graph: TopologicalGraph<>
        }
    }

    fun unregisterListener(listener: Any) {
        handlerListLock.withLock {

        }
    }

    fun isListenerRegistered(listener: Any): Boolean {
        handlerListLock.withLock {
            for (value in handlerList) {
                if (value.listenerObj === listener) return true
            }
            return false
        }
    }


    private class EventHandler<E>(val name: String, val listenerObj: Any?, val eventType: Class<E>, val function: (E) -> Unit) {
        var mustRunAfter = ArrayList<String>()
        var mustRunBefore = ArrayList<String>()
    }

}