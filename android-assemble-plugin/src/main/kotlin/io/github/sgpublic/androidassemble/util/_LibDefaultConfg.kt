package io.github.sgpublic.androidassemble.util

import com.android.build.api.dsl.LibraryDefaultConfig

private val versionCodes = hashMapOf<LibraryDefaultConfig, Int>()
private val versionNames = hashMapOf<LibraryDefaultConfig, String>()

/** 为 com.android.library 添加版本号，并写入 BuildConfig */
var LibraryDefaultConfig.libVersionCode: Int
    get() = versionCodes[this] ?: throw RuntimeException(
        "Please defined android.defaultConfig.libVersionCode"
    )
    set(value) {
        buildConfigField("VERSION_CODE", value)
        versionCodes[this] = value
    }

/** 为 com.android.library 添加版本名，并写入 BuildConfig */
var LibraryDefaultConfig.libVersionName: String
    get() = versionNames[this] ?: throw RuntimeException(
        "Please defined android.defaultConfig.libVersionName"
    )
    set(value) {
        buildConfigField("VERSION_NAME", value)
        versionNames[this] = value
    }
