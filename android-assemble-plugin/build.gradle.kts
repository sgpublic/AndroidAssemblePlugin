import io.github.sgpublic.androidassemble.gradlePluginPublish
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `kotlin-dsl`

    `java-gradle-plugin`
    id("com.gradle.plugin-publish")
}

dependencies {
    // https://mvnrepository.com/artifact/com.android.tools.build/gradle-api
    implementation("com.android.tools.build:gradle-api:7.0.0")
    // https://mvnrepository.com/artifact/io.github.sgpublic/SimplifyXMLObject
    implementation("io.github.sgpublic:SimplifyXMLObject:1.2.2")
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

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}

