package io.github.sgpublic.androidassemble.util

import java.io.InputStream

/**
 * @author Madray Haven
 * @Date 2023/8/16 13:50
 */

inline fun <T> withRuntime(command: String, crossinline block: Process.() -> T): T {
    val runtime = Runtime.getRuntime()
    val result = block.invoke(runtime.exec(command))
    runtime.exit(0)
    return result
}

inline fun InputStream.useFirstLineIf(crossinline block: (String) -> Boolean): String? {
    reader().use { reader ->
        val firstLine = StringBuilder().also { builder ->
           var char: Char
           while (reader.read().also { char = it.toChar() } >= 0) {
               if (char == '\n') {
                   break
               }
               builder.append(char)
           }
        }.toString()
        if (block.invoke(firstLine)) {
            return firstLine
        } else {
            return null
        }
    }
}

fun InputStream.useFirstLineIfNotBlack(): String? {
    return useFirstLineIf {
        it.isNotBlank()
    }
}