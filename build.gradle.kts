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
    kotlin("jvm") version "1.7.10"
    id ("com.github.johnrengelman.shadow") version "7.0.0"
    id("java-library")
    `maven-publish`
}

group = "me.daarkii"
version = "1.0.0"

repositories {
    mavenCentral()
}

apply {
    plugin("org.jetbrains.kotlin.jvm")
    plugin("com.github.johnrengelman.shadow")
    plugin("maven-publish")
}

dependencies {

    api("com.zaxxer", "HikariCP", "3.4.5")

    api("org.mongodb", "mongo-java-driver", "3.12.10")

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ShadowJar> {
    archiveFileName.set("database-adapter-$version.jar")
}

//publish test
java {
    withSourcesJar()
    withJavadocJar()
}

publishing {

    publications {
        create<MavenPublication>("maven") {
            artifact("build/libs/database-adapter-$version.jar") {
                extension = ".jar"
            }
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
