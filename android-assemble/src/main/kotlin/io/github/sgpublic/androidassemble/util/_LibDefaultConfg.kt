package io.github.sgpublic.androidassemble.util

import com.android.build.api.dsl.LibraryDefaultConfig

/** 为 com.android.library 添加版本号，并写入 BuildConfig */
var LibraryDefaultConfig.libVersionCode: Int?
    get() = null
    set(value) {
        buildConfigField("VERSION_CODE", value ?: return)
    }

/** 为 com.android.library 添加版本名，并写入 BuildConfig */
var LibraryDefaultConfig.libVersionName: String?
    get() = null
    set(value) {
        buildConfigField("VERSION_NAME", value ?: return)
    }
