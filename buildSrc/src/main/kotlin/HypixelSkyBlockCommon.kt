import groovy.lang.Closure
import org.gradle.api.Plugin
import org.gradle.api.Project

class HypixelSkyBlockCommon: Plugin<Project> {

    override fun apply(project: Project) {
        project.version = "1.0-SNAPSHOT"
        project.group = "me.fan87"

        project.repositories.apply {
            mavenLocal()
            mavenCentral()
        }
    }

}