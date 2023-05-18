package io.github.sgpublic.gradle.util

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import io.github.sgpublic.gradle.core.DefaultAssembleOption
import io.github.sgpublic.gradle.core.assembleOption
import io.github.sgpublic.gradle.core.renameRule
import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem
import java.io.File
import java.io.InputStream
import java.io.OutputStream

internal fun Project.applyAssemble() {
    when (val android = project.extensions.getByName("android")) {
        is AppExtension -> android.applicationVariants.all {
            applyAssembleIntern(project)
        }
        is LibraryExtension -> android.libraryVariants.all {
            applyAssembleIntern(project)
        }
    }
}

private fun BaseVariant.applyAssembleIntern(project: Project) {
    for (output in outputs) {
        if (output !is BaseVariantOutputImpl) {
            continue
        }
        val name = output.name.split("-")
            .joinToString("") { it.capitalize() }
        project.tasks.register("assemble${name}AndLocate") {
            dependsOn("assemble${name}")
            doLast {
                project.doLastAssemble(this@applyAssembleIntern, output)
            }
        }
    }
}

private fun Project.doLastAssemble(variant: BaseVariant, output: BaseVariantOutputImpl) {
    if (!output.outputFile.exists()) {
        return
    }
    val assemble = File(
        (assembleOption[this] ?: DefaultAssembleOption).getOutputDir(variant),
        variant.flavorName
    )
    val outputName = variant.buildType.renameRule.invoke(variant)

    val copy = File(assemble, outputName)
    output.outputFile.copy(copy)

    val current = OperatingSystem.current()
    val runtime = Runtime.getRuntime()
    when {
        current.isWindows -> runtime.exec("explorer.exe /select, $copy")
    }
}


private fun File.copy(target: File): File {
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