package me.fan87.hypixelskyblock.events

import me.fan87.hypixelskyblock.main.SkyBlock
import me.fan87.hypixelskyblock.utils.Logger
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.reflections.Reflections
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

object EventsManager {

    private val handlerList = HashMap<String, ArrayList<EventCallable>>()
    private val registeredListeners = ArrayList<Any>()

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

    }

    fun registerListener(listener: Any) {
        for (method in listener::class.java.methods) {
            if (method.isAnnotationPresent(Subscribe::class.java)) {
                if (method.parameterCount != 1) {
                    Logger.warning("Invalid Event Handler: ${listener::class.java.name}.${method.name}")
                    continue
                }
                var eventType = method.parameterTypes[0]

                val element = object : EventCallable(listener, 0) {
                    override fun call(event: Any) {
                        method.invoke(listener, event)
                    }
                }

                handlerListLock.withLock {
                    while (eventType != null) {
                        val eventCallables = handlerList.getOrDefault(eventType.name, ArrayList())
                        eventCallables.add(element)
                        handlerList[eventType.name] = eventCallables
                        eventType = eventType.superclass
                    }
                }
            }
        }
        handlerListLock.withLock {
            registeredListeners.add(listener)
        }
    }

    fun unregisterListener(listener: Any) {
        handlerListLock.withLock {
            if (!registeredListeners.contains(listener)) {
                return
            }
            for (entry in HashMap(handlerList)) {
                for (eventCallable in entry.value.withIndex()) {
                    if (eventCallable.value.listenerObject === listener) {
                        val eventCallables = handlerList[entry.key]!!
                        eventCallables.removeAt(eventCallable.index)
                        handlerList[entry.key] = eventCallables
                    }
                }
            }
        }
    }

    fun isListenerRegistered(listener: Any): Boolean {
        handlerListLock.withLock {
            return registeredListeners.any { it === listener }
        }
    }


    private abstract class EventCallable(val listenerObject: Any, val priority: Int) {
        abstract fun call(event: Any)
    }

}