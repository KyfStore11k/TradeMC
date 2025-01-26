plugins {
    kotlin("jvm") version "2.1.20-Beta1"
    id("com.gradleup.shadow") version "9.0.0-beta6"
}

group = "com.kyfstore"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://libraries.minecraft.net") { }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("com.mojang:brigadier:1.0.18")
    compileOnly("org.jetbrains:annotations:26.0.2")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.google.guava:guava:33.4.0-jre")
    api("com.google.guava:guava:33.4.0-jre")
    implementation("com.google.guava:guava:33.4.0-android")
    api("com.google.guava:guava:33.4.0-android")

    implementation("me.lucko:commodore:2.2")
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks {
    build {
        dependsOn("shadowJar")
    }

    shadowJar {
        dependencies {
            exclude(dependency("com.mojang:brigadier"))
        }
    }

    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    jar {
        from(sourceSets.main.get().output)
    }
}

sourceSets {
    val main by getting {
        java.srcDirs("src/main/kotlin")
    }
}