import project.ReadMeChapterGenerator

plugins {

}

apply<HypixelSkyBlockCommon>()



tasks {
    val generateReadMe by register<ReadMeChapterGenerator>("generateReadMe") {
        group = "readme"
    }
}

