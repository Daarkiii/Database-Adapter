import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id ("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "me.daarkii"
version = "1.0.0"

repositories {
    mavenCentral()
}

apply {
    plugin("org.jetbrains.kotlin.jvm")
    plugin("com.github.johnrengelman.shadow")
}

dependencies {

    api("com.zaxxer", "HikariCP", "3.4.5")

    api("org.mongodb", "mongo-java-driver", "3.12.10")

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}