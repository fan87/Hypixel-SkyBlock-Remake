plugins {
    kotlin("jvm") version "1.6.20"
}


repositories {
    mavenCentral()
}

dependencies {
    implementation("nl.vv32.rcon:rcon:1.1.0")
    implementation("org.ow2.asm:asm-util:9.3")
    implementation("org.ow2.asm:asm:9.3")

    implementation("org.yaml:snakeyaml:1.30")
}
