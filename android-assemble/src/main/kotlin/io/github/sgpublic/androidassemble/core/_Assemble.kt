package io.github.sgpublic.androidassemble.core

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import io.github.sgpublic.androidassemble.AndroidAssemblePlugin
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
        (assembleOption[this] ?: DefaultAssembleOption).getOutputDir(),
        variant.flavorName
    )
    val outputName = (variant.buildType.renameRule?.invoke(
        RenameParam(
        flavorType = variant.flavorName,
        buildType = variant.buildType.name,
        versionName = variant.mergedFlavor.versionName ?: "",
        versionCode = variant.mergedFlavor.versionCode ?: 1,
    )
    ) ?: (AndroidAssemblePlugin.RootProject.name + "-" + AndroidAssemblePlugin.Project.name + when (variant.buildType.name) {
        "release" -> " V${variant.mergedFlavor.versionName ?: return}(${variant.mergedFlavor.versionCode ?: return})"
        "debug" -> "_${variant.mergedFlavor.versionName ?: return}_${variant.mergedFlavor.versionCode ?: return}"
        else -> return
    })) + ".${output.outputFile.extension}"

    val copy = File(assemble, outputName)
    output.outputFile.copy(copy)

    val current = OperatingSystem.current()
    val runtime = Runtime.getRuntime()
    when {
        current.isWindows -> runtime.exec("explorer.exe /select, $copy")
        current.isMacOsX -> runtime.exec("open -R $copy")
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