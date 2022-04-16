package me.fan87.hypixelskyblock.events

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Subscribe(val mustRunAfter: Array<String>, val mustRunBefore: Array<String>)
