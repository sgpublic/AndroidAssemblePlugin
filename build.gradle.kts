plugins {
    val androidVer = "7.4.2"
    id("com.android.application") version androidVer apply false
    id("com.android.library") version androidVer apply false

    val kotlinVer = "1.8.21"
    kotlin("android") version kotlinVer apply false
    kotlin("jvm") version kotlinVer apply false
    kotlin("plugin.parcelize") version kotlinVer apply false
    kotlin("kapt") version kotlinVer apply false
    kotlin("plugin.lombok") version kotlinVer apply false

    id("com.gradle.plugin-publish") version "1.2.0" apply false

    id("io.github.sgpublic.android-assemble") version "2.0.0-beta02" apply false
}