package io.github.sgpublic.androidassemble.util

import com.android.build.api.dsl.VariantDimension

fun VariantDimension.buildConfigField(name: String, value: Any) {
    when (value) {
        is String -> buildConfigField("String", name, "\"$value\"")
        is Int -> buildConfigField("int", name, value.toString())
        is Long -> buildConfigField("long", name, value.toString())
        is Float -> buildConfigField("float", name, value.toString())
        is Double -> buildConfigField("double", name, value.toString())
        is Boolean -> buildConfigField("boolean", name, value.toString())
        else -> throw RuntimeException("Unsupported type: ${value.javaClass}")
    }
}