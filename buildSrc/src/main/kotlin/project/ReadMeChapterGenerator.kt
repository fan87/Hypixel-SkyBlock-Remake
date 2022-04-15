package project

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.charset.Charset


open class ReadMeChapterGenerator: DefaultTask() {

    @Internal
    var chapterTag = "// INSERT_CHAPTER_TAG"


    @get:InputFile
    val inputFile: RegularFileProperty = project.objects.fileProperty()
    @get:OutputFile
    val outputFile: RegularFileProperty = project.objects.fileProperty()

    @get:Input
    val charset: Property<String> = project.objects.property(String::class.java)

    init {
        inputFile.convention{File(project.rootProject.rootDir, "readme.raw.md")}
        outputFile.convention{File(project.rootProject.rootDir, "readme.md")}
        charset.convention(Charset.defaultCharset().name())
    }

    @TaskAction
    fun generate() {
        val input = inputFile.asFile.get()
        val output = outputFile.asFile.get()

        var inCodeBlock = false

        val inputText = input.readText(Charset.forName(charset.get()))

        val namesCount = HashMap<String, Int>().withDefault { -1 }

        var hasFoundChapterTag = false

        var beforeInputBuffer = ""
        var afterInputBuffer = ""
        var chaptersBuffer = ""

        val lines = inputText.lines()
        for (line in lines.withIndex()) {
            if (line.value.startsWith("```")) {
                inCodeBlock = !inCodeBlock
            }
            if (line.value == chapterTag) {
                hasFoundChapterTag = true
                continue
            }
            if (!hasFoundChapterTag) {
                beforeInputBuffer += line.value + if (line.index != lines.lastIndex) "\n" else ""
            } else {
                afterInputBuffer += line.value + if (line.index != lines.lastIndex) "\n" else ""
            }
            if (!inCodeBlock && line.value.matches(Regex("#{1,} .*"))) {
                val level = line.value.indexOf(" ") - 1
                var originalName = line.value.substring(level + 2)
                var ancherName = originalName
                // Normalize it (if that's how you use this word)
                ancherName = ancherName.lowercase()
                ancherName = ancherName.replace(" ", "-")
                ancherName = ancherName.replace(Regex("[^a-z-]"), "")

                // Process duplicated ancher name
                var count = namesCount.getValue(ancherName) + 1
                namesCount[ancherName] = count

                if (count != 0) {
                    ancherName += "-${count}"
                }

                chaptersBuffer += " - "*(level) + "[$originalName](#$ancherName)" + "\n"
            }
        }

        output.writeText("\n<!-- You shouldn't be editing this file! Edit `readme.raw.md` instead! -->\n\n"*10 +
                "${beforeInputBuffer}\n$chaptersBuffer\n$afterInputBuffer" +
                "\n\n<!-- You shouldn't be editing this file! Edit `readme.raw.md` instead! -->\n"*10, Charset.forName(charset.get()))

    }

}

operator fun String.times(times: Int): String {
    var buffer = ""
    for (i in 0 until times) {
        buffer += this
    }
    return buffer
}
