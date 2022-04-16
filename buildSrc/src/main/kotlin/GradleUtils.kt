
import nl.vv32.rcon.Rcon
import org.gradle.api.Project
import java.io.File
import java.io.IOException
import java.net.URL
import java.security.MessageDigest
import java.util.Base64


object GradleUtils {

    fun getServerJar(project: Project): File {
        val buildToolsDir = File(project.rootProject.rootDir, "BuildTools/")
        val buildToolsFile = File(buildToolsDir, "BuildTools.jar")
        val buildOutputFile = File(buildToolsDir, "spigot-1.8.8.jar")
        val buildToolsDownloadUrl = URL("https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar")
        project.logger.info("Attempting to resolve server jar...")

        if (!buildOutputFile.exists()) {
            project.logger.info("BuildTools output is not found! Running build tools...")
            if (buildToolsDir.exists()) buildToolsDir.delete()
            buildToolsDir.mkdirs()
            if (buildToolsFile.exists()) buildToolsFile.createNewFile()
            project.logger.info("Downloading BuildTools to build Spigot")
            val stream = buildToolsDownloadUrl.openStream()
            buildToolsFile.writeBytes(stream.readBytes())
            stream.close()
            project.logger.info("Running BuildTools...")
            project.javaexec {
                it.workingDir(buildToolsDir.absolutePath)
                it.args("--rev", "1.8.8", "--disable-certificate-check")
                it.classpath = project.files(buildToolsFile.absolutePath)
                it.mainClass.set("org.spigotmc.builder.Bootstrap")
                if (!System.getProperty("os.name").lowercase().contains("win")) {
                    it.environment("SHELL", "/usr/bin/bash") // If user is running different shell, it won't break
                }
            }
            project.logger.info("Checking if output exists...")
            if (!buildOutputFile.exists()) {
                throw IOException("BuildTool has failed to run!")
            }
        }
        return buildOutputFile
    }

    // Generates an unique password
    fun getRconPassword(): String {
        val digest = MessageDigest.getInstance("SHA-256")
        return Base64.getEncoder().encodeToString(digest.digest(System.getProperty("user.dir").toByteArray()))
    }


    fun execute(vararg commands: String): Array<String> {
        Rcon.open("localhost", 25575).use { rcon ->
            if (rcon.authenticate(getRconPassword())) {
                return Array(commands.size) { i ->
                    rcon.sendCommand(commands[i])
                }
            } else {
                throw IOException("Failed to authenticate!")
            }
        }
    }

}