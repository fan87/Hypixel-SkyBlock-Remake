import com.github.jengelman.gradle.plugins.shadow.tasks.*

plugins {
    kotlin("jvm") version "1.6.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

apply<HypixelSkyBlockCommon>()

tasks.shadowJar {
    archiveClassifier.set("")
}

tasks.getByName("build").dependsOn("shadowJar")



dependencies {
    compileOnly(files(GradleUtils.getServerJar(project).absolutePath))

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.20")
    implementation("org.mongodb:mongodb-driver-sync:4.5.1")

    implementation("org.reflections:reflections:0.10.2")
}

tasks.processResources {
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand("version" to version)
    }
}

tasks {

    register("reloadPlugin") {
        group = "run"
        description = "Reload the main plugin"

        val pluginFile: File = project.tasks.getByName<Jar>("jar").archiveFile.get().asFile

        dependsOn("build")

        inputs.files(pluginFile)
        doLast {
            val runDir = File(rootProject.projectDir, "run/")
            val destPluginFile = File(runDir, "/plugins/hypixel-skyblock-plugin-DEV.jar")
            if (!destPluginFile.parentFile.exists()) {
                destPluginFile.parentFile.mkdirs()
            }
            if (!destPluginFile.exists()) {
                destPluginFile.createNewFile()
            }
            destPluginFile.writeBytes(pluginFile.readBytes())
            println("Server Responded ${GradleUtils.execute("reload")[0]}")
        }
    }

}


