import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven(url = "https://m2.dv8tion.net/releases")  // For JDA and related dependencies
    maven(url = "https://jitpack.io") // For other dependencies
    maven { url = uri("https://repo.lavalink.dev/releases") } // Official Lavalink repository
}

application {
    mainClass.set("discordBot.SteJoBott")
}

dependencies {
    implementation("net.dv8tion:JDA:5.5.1")
    implementation("ch.qos.logback:logback-classic:1.5.13")
    implementation("io.github.cdimascio:dotenv-java:3.0.0")

    // Lavalink client interaction
    implementation("dev.arbjerg:lavaplayer:2.2.3")  // Use Lavaplayer to interact with Lavalink
}

tasks.named<ShadowJar>("shadowJar") {
    archiveFileName.set("discordBot-1.0.jar")
    archiveClassifier.set("")
}
