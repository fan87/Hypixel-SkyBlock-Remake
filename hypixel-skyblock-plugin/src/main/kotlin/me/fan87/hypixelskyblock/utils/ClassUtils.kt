package me.fan87.hypixelskyblock.utils

import org.apache.logging.log4j.core.config.plugins.ResolverUtil
import org.apache.logging.log4j.core.config.plugins.ResolverUtil.ClassTest

object ClassUtils {

    fun listAllClasses(filter: (clazz: Class<*>) -> Boolean, packageName: String) {
        val resolverUtil = ResolverUtil()
        resolverUtil.findInPackage(object: ClassTest() {
            override fun matches(p0: Class<*>?): Boolean {
                if (p0 == null) return false
                return filter(p0)
            }
        }, packageName)
    }

}