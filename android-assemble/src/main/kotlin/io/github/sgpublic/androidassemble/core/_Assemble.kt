package io.github.sgpublic.androidassemble.core

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.ApplicationVariant
import com.android.build.api.variant.BuildConfigField
import com.android.build.api.variant.LibraryVariant
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.android.build.gradle.api.VersionedVariant
import com.android.tools.r8.internal.it
import io.github.sgpublic.androidassemble.AndroidAssemblePlugin
import io.github.sgpublic.androidassemble.internal.copy
import org.gradle.api.Project
import org.gradle.api.provider.MapProperty
import org.gradle.internal.os.OperatingSystem
import java.io.File
import java.io.Serializable

class ApplyAction internal constructor(private val project: Project) {
    internal fun applyAssemble() {
        when (val android = project.extensions.getByName("android")) {
            is ApplicationExtension -> applyAssembleIntern(android)
            is LibraryExtension -> applyAssembleIntern(android)
            else -> {
                AndroidAssemblePlugin.Logger.error("Unknown Extension type: ${android.javaClass}")
                return
            }
        }
    }

    private fun applyAssembleIntern(variant: CommonExtension<*,*,*,*>) {
        for (output in variant.outputs) {
            val name = output.name.split("-")
                .joinToString("") { it.capitalize() }
            project.tasks.register("assemble${name}AndLocate") {
                dependsOn("assemble${name}")
                doLast {
                    doLastAssemble(variant, output)
                }
            }
        }
    }

    private fun doLastAssemble(variant: BaseVariant, output: BaseVariantOutput) {
        if (!output.outputFile.exists()) {
            AndroidAssemblePlugin.Logger.error("Output file not found")
            return
        }
        val assemble = File(
            (assembleOption[project] ?: AssembleOptionImpl(project)).getOutputDir(),
            variant.flavorName
        )
        val outputName = (variant.buildType.renameRule ?: {
            AndroidAssemblePlugin.RootProject.name + "-" + project.name + when (flavorType) {
                "release" -> " V${versionName}(${versionCode})"
                else -> "_${versionName}_${versionCode}"
            }
        }).invoke(
            when (variant) {
                is ApplicationVariant -> {
                    AndroidAssemblePlugin.Logger.debug("variant type: {}", ApplicationVariant::class.java)
                    RenameParam(
                        flavorType = variant.getFlavorName(),
                        buildType = variant.getBuildType().name,
                        versionName = variant.buildConfigFields["VERSION_NAME"]!!.toString(),
                        versionCode = variant.buildConfigFields["VERSION_CODE"]!!.toString().toInt(),
                    )
                }
                is LibraryVariant -> {
                    AndroidAssemblePlugin.Logger.debug("variant type: {}", LibraryVariant::class.java)
                    RenameParam(
                        flavorType = variant.getFlavorName(),
                        buildType = variant.getBuildType().name,
                        versionName = variant.buildConfigFields["VERSION_NAME"]?.toString() ?: "undefined",
                        versionCode = variant.buildConfigFields["VERSION_CODE"]?.toString()?.toInt() ?: -1,
                    )
                }
                is VersionedVariant -> {
                    AndroidAssemblePlugin.Logger.debug("variant type: {}", VersionedVariant::class.java)
                    RenameParam(
                        flavorType = variant.flavorName,
                        buildType = variant.buildType.name,
                        versionName = variant.versionName,
                        versionCode = variant.versionCode,
                    )
                }
                is com.android.build.gradle.api.LibraryVariant -> {
                    AndroidAssemblePlugin.Logger.debug("variant type: {}", com.android.build.gradle.api.LibraryVariant::class.java)
                    RenameParam(
                        flavorType = variant.getFlavorName(),
                        buildType = variant.getBuildType().name,
                        versionName = variant.mergedFlavor.versionName ?: "undefined",
                        versionCode = variant.mergedFlavor.versionCode ?: -1,
                    )
                }
                else -> throw UnknownError("Unknown Variant type: ${variant.javaClass}")
            }
        ) + ".${output.outputFile.extension}"

        val copy = File(assemble, outputName)
        output.outputFile.copy(copy)

        val current = OperatingSystem.current()
        val runtime = Runtime.getRuntime()
        when {
            current.isWindows -> runtime.exec("explorer.exe /select, $copy")
            current.isMacOsX -> runtime.exec("open -R $copy")
        }
    }
}

operator fun MapProperty<String, BuildConfigField<out Serializable>>.get(key: String) =
    getting(key).orNull?.value