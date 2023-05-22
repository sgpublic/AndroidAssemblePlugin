import io.github.sgpublic.androidassemble.gradlePluginPublish

plugins {
    kotlin("jvm")
    `kotlin-dsl`

    `java-gradle-plugin`
    id("com.gradle.plugin-publish")
}

dependencies {
    val agp = "7.0.0"
    implementation("com.android.tools.build:gradle-api:$agp")
}

gradlePluginPublish("android-assemble") {
    implementationClass = "io.github.sgpublic.androidassemble.AndroidAssemblePlugin"
    displayName = "Plugin for apk and aar packaging"
    description = "A plugin that provides extension capabilities for apk and aar packaging"
    tags.set(listOf("android"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}