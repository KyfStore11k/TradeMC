plugins {
    kotlin("jvm") version "2.1.20-Beta1"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.11"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.freefair.lombok") version "8.12.1"
    id("com.github.ben-manes.versions") version "0.52.0"
    id("com.gradleup.shadow") version "9.0.0-beta4"
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
}

group = "com.kyfstore"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven { name = "papermc-repo"; url = uri("https://repo.papermc.io/repository/maven-public/") }
    maven { name = "sonatype"; url = uri("https://oss.sonatype.org/content/groups/public/") }

    maven("https://repo.xenondevs.xyz/releases")
    maven("https://libraries.minecraft.net") { }
    maven("https://jitpack.io") { }
    maven("https://repo.codemc.org/repository/maven-public/") { }
}

dependencies {

    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")

    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("com.mojang:brigadier:1.0.18")
    compileOnly("org.jetbrains:annotations:26.0.2")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        exclude(group = "org.bukkit", module = "bukkit")
    }
    compileOnly("xyz.xenondevs.invui:invui:1.44")
    compileOnly("net.luckperms:api:5.4")

    implementation("dev.jorel:commandapi-bukkit-shade-mojang-mapped:9.7.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation(gradleApi())
    implementation("me.lucko:commodore:2.2")
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

//paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION
//paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

tasks {
    build {
        dependsOn("shadowJar")
    }

    shadowJar {
        relocate("dev.jorel.commandapi", "com.kyfstore.tradeMC.commandapi")

        dependencies {
            exclude(dependency("com.mojang:brigadier"))
        }
        manifest {
            attributes["paperweight-mappings-namespace"] = "mojang"
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
        manifest {
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
    }

    assemble {
        dependsOn("reobfJar")
    }
}

sourceSets {
    val main by getting {
        java.srcDirs("src/main/kotlin")
    }
}