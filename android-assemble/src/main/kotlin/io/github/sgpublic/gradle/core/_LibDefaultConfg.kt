package io.github.sgpublic.gradle.core

import com.android.build.gradle.internal.dsl.DefaultConfig
import io.github.sgpublic.gradle.util.buildConfigField

var DefaultConfig.libVersionCode: Int?
    get() = null
    set(value) {
        buildConfigField("VERSION_CODE", value ?: return)
    }
var DefaultConfig.libVersionName: String?
    get() = null
    set(value) {
        buildConfigField("VERSION_NAME", value ?: return)
    }