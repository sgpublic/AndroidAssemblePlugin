package io.github.sgpublic.androidassemble.core

import com.android.build.gradle.internal.dsl.DefaultConfig
import io.github.sgpublic.androidassemble.util.buildConfigField

var DefaultConfig.libVersionCode: Int?
    get() = null
    set(value) {
        versionCode = value
        buildConfigField("VERSION_CODE", value ?: return)
    }
var DefaultConfig.libVersionName: String?
    get() = null
    set(value) {
        versionName = value
        buildConfigField("VERSION_NAME", value ?: return)
    }