/*
 * Copyright 2022 Daarkii & contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10" //kotlin
    id ("com.github.johnrengelman.shadow") version "7.0.0" //build the jar
    id("java-library") //need for publishing
    `maven-publish` //need for publishing
    id("org.jetbrains.dokka") version "1.7.10" //need for javadoc building
}

group = "me.daarkii"
version = "1.0.3"

repositories {
    mavenCentral()
}

apply {
    plugin("org.jetbrains.kotlin.jvm")
    plugin("com.github.johnrengelman.shadow")
    plugin("maven-publish")
    plugin("org.jetbrains.dokka")
}

dependencies {

    //mysql
    api("com.zaxxer", "HikariCP", "3.4.5")

    //mongoDB
    api("org.mongodb", "mongo-java-driver", "3.12.10")

    //needed to create javaDocs
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.10")
}

/**
 * Build settings
 */

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ShadowJar> {
    archiveFileName.set("database-adapter-$version.jar")
    exclude("*.pom")
    exclude("**/*.kotlin_metadata")
    exclude("**/*.kotlin_module")
    exclude("**/*.kotlin_builtins")
}

val shadowJar: ShadowJar by tasks
val javadoc: Javadoc by tasks
val jar: Jar by tasks
val build: Task by tasks
val clean: Task by tasks

val sourcesForRelease = task<Copy>("sourcesForRelease") {
    from("src/main/kotlin")
    into("build/filteredSrc")
    includeEmptyDirs = false
}

val sourcesJar = task<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourcesForRelease.destinationDir)

    dependsOn(sourcesForRelease)
}

//Create javadocs
val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn(dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaHtml.outputDirectory)
}

//build all files (normal, javadoc, source)
build.apply {
    dependsOn(jar)
    dependsOn(javadocJar)
    dependsOn(sourcesJar)
    dependsOn(shadowJar)

    jar.mustRunAfter(clean)
    shadowJar.mustRunAfter(sourcesJar)
}

/**
 * Publish settings
 */

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {

    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "nexus"

            url = if(version.toString().contains("SNAPSHOT"))
                uri("https://repo.aysu.tv/repository/snapshots/")
            else
                uri("https://repo.aysu.tv/repository/releases/")

            credentials {
                username = System.getenv("NEXUS_USERNAME")
                password = System.getenv("NEXUS_PASSWORD")
            }
        }
    }
}
