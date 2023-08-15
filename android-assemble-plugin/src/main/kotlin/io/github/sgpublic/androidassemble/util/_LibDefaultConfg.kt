package io.github.sgpublic.androidassemble.util

import com.android.build.gradle.internal.dsl.DefaultConfig
import io.github.sgpublic.androidassemble.util.buildConfigField

/** 为 com.android.library 添加版本号，并写入 BuildConfig */
var DefaultConfig.libVersionCode: Int?
    get() = null
    set(value) {
        versionCode = value
        buildConfigField("VERSION_CODE", value ?: return)
    }

/** 为 com.android.library 添加版本名，并写入 BuildConfig */
var DefaultConfig.libVersionName: String?
    get() = null
    set(value) {
        versionName = value
        buildConfigField("VERSION_NAME", value ?: return)
    }