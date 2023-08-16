package io.github.sgpublic.androidassemble.util

import java.io.InputStream

/**
 * @author Madray Haven
 * @Date 2023/8/16 13:50
 */

inline fun <T> withRuntime(command: String, crossinline block: Process.() -> T): T {
    val process = Runtime.getRuntime().exec(command)
    val result = block.invoke(process)
    process.destroy()
    return result
}

fun InputStream.useFirstLineIf(block: (String) -> Boolean): String? {
    val firstLine = reader().readLines()
        .takeIf { it.isNotEmpty() }
        ?.get(0)
    return firstLine?.takeIf { block.invoke(it) }
}

fun InputStream.useFirstLineIfNotBlack(): String? {
    return useFirstLineIf {
        it.isNotBlank()
    }
}