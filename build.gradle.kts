plugins {
    application
    id("com.gradleup.shadow") version "8.3.1"
}

application {
    mainClass.set("discordBot.SteJoBott")
}

group = "org.example"
version = "1.0"

val jdaVersion = "5.5.1"

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://m2.dv8tion.net/releases")
}

dependencies {
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation("ch.qos.logback:logback-classic:1.5.13")
    implementation("io.github.cdimascio:dotenv-java:3.0.0") // Load .env file at runtime
    implementation("com.sedmelluq:lavaplayer:1.3.78")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.isIncremental = true
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}