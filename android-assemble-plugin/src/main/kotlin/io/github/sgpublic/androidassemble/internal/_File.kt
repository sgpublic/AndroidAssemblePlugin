package io.github.sgpublic.androidassemble.internal

import org.gradle.internal.os.OperatingSystem
import java.io.File
import java.io.InputStream
import java.io.OutputStream

internal fun File.copy(target: File): File {
    if (!exists()) {
        throw NoSuchFileException(this)
    }
    if (target.exists() && !target.delete()) {
        throw FileAlreadyExistsException(this, target, "Tried to overwrite the destination, but failed to delete it.")
    }
    if (isDirectory) {
        throw IllegalStateException("Copy a directory not support!")
    }

    if (target.parentFile != null) {
        target.parentFile.mkdirs()
    }

    inputStream().copy(target.outputStream())
    return target
}

private val buffer = ByteArray(8 * 1024)
private fun InputStream.copy(target: OutputStream): Long {
    var bytesCopied = 0L
    var bytes = read(buffer)
    while(bytes >= 0) {
        target.write(buffer, 0, bytes)
        bytesCopied += bytes
        bytes = read(buffer)
    }
    close()
    target.flush()
    target.close()
    return bytesCopied
}

fun File.locate() {
    val current = OperatingSystem.current()
    val runtime = Runtime.getRuntime()
    when {
        current.isWindows -> runtime.exec("explorer.exe /select, $canonicalPath")
        current.isMacOsX -> runtime.exec("open -R $canonicalPath")
    }
}