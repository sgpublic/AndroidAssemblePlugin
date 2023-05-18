import io.github.sgpublic.gradle.gradlePluginPublish

plugins {
    kotlin("jvm")
    `kotlin-dsl`

    `java-gradle-plugin`
    id("com.gradle.plugin-publish")
}

dependencies {
    val agp = "4.2.0"
    implementation("com.android.application:com.android.application.gradle.plugin:$agp")
    implementation("com.android.library:com.android.library.gradle.plugin:$agp")
}

gradlePluginPublish("android-assemble") {
    implementationClass = "io.github.sgpublic.gradle.AndroidAssemblePlugin"
    displayName = "Plugin for apk and aar packaging"
    description = "A plugin that provides extension capabilities for apk and aar packaging"
    tags.set(listOf("android"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}