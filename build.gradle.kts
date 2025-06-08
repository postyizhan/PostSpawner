plugins {
    kotlin("jvm") version "1.8.21"
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "com.github.postyizhan"
version = "1.4"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
        name = "spigot-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    // Vault 仓库
    maven("https://jitpack.io") {
        name = "jitpack"
    }
    // PlaceholderAPI 仓库
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") {
        name = "placeholderapi"
    }
    // ItemsAdder 仓库
    maven("https://jitpack.io")
    // NeigeItems 仓库
    maven("https://r.irepo.space/maven/")
    // MMOItems 仓库
    maven("https://nexus.phoenixdevt.fr/repository/maven-public/")
    // Oraxen 仓库
    maven("https://repo.oraxen.com/releases")
    // MythicMobs 仓库
    maven("https://mvn.lumine.io/repository/maven-public/")
    // Zaphkiel 仓库
    maven("https://repo.tabooproject.org/repository/releases/")
    // CraftEngine 仓库
    maven("https://repo.momirealms.net/releases/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Adventure API (MiniMessage支持)
    implementation("net.kyori:adventure-api:4.14.0")
    implementation("net.kyori:adventure-platform-bukkit:4.3.0") 
    implementation("net.kyori:adventure-text-minimessage:4.14.0")

    // Vault API
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        exclude(group = "org.bukkit", module = "bukkit")
    }
    
    // PlaceholderAPI
    compileOnly("me.clip:placeholderapi:2.11.6")
    
    // PlayerPoints API - 通过反射调用
    
    // 物品库依赖
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.1")
    compileOnly("pers.neige.neigeitems:NeigeItems:1.15.96")
    compileOnly("net.Indyuce:MMOItems-API:6.9.5-SNAPSHOT")
    compileOnly("io.th0rgal:oraxen:1.171.0")
    compileOnly("io.lumine:Mythic-Dist:5.4.1")
    compileOnly("ink.ptms:Zaphkiel:2.0.14")
    compileOnly("net.momirealms:craft-engine-core:0.0.22")
    compileOnly("net.momirealms:craft-engine-bukkit:0.0.22")
    
    // 本地libs目录依赖 - 这些API只在编译时使用，运行时由插件提供
    compileOnly(files(
        "libs/AzureFlow-1.0.2.4-rc1-build-41212-all.min.jar",
        "libs/SX-Item-3.2.4-all.min.jar"
        )
    )
    
    // 暂时注释掉不兼容的Kotlin库
    // "libs/Ratziel-A.0.4.2.0.jar",
    // "libs/ItemTools-3.0.2-api.jar"
    
    // bStats
    implementation("org.bstats:bstats-bukkit:3.0.2")
}

tasks {
    runServer {
        // 修改Minecraft版本
        minecraftVersion("1.13.2")
    }
    
    shadowJar {
        archiveBaseName.set("PostSpawner")
        archiveClassifier.set("")
        archiveVersion.set(project.version.toString())
        
        // 重定位bStats类
        relocate("org.bstats", "com.github.postyizhan.util.bstats")
        // 重定位Adventure API
        relocate("net.kyori", "com.github.postyizhan.util.kyori")
    }
}

val targetJavaVersion = 8
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}
