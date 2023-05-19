package io.github.sgpublic.androidassemble.core

import io.github.sgpublic.androidassemble.AndroidAssemblePlugin
import org.gradle.api.Project
import java.io.File
import java.io.Serializable

interface AssembleOption {
    var outputDir: Any

    fun getOutputDir(): File {
        return when (outputDir) {
            is File -> outputDir as File
            is String -> File(outputDir as String)
            else -> throw RuntimeException("AssembleOption#outputDir can only be File or String")
        }
    }
}

internal open class AssembleOptionImpl: AssembleOption {
    override var outputDir: Any = File(
        AndroidAssemblePlugin.RootProject.projectDir,
        "./assemble/${AndroidAssemblePlugin.Project.name}"
    )
}

internal object DefaultAssembleOption: AssembleOptionImpl()

internal val assembleOption = hashMapOf<Project, AssembleOption>()
fun Project.assembleOption(block: AssembleOption.() -> Unit) {
    assembleOption[this] = AssembleOptionImpl().also(block)
}

data class RenameParam(
    val flavorType: String,
    val buildType: String,
    val versionName: String = "",
    val versionCode: Int = 1,
)


typealias BaseRenameRule = (RenameParam).() -> String

private val renameRules = hashMapOf<String, BaseRenameRule>()
fun com.android.build.api.dsl.BuildType.renameRule(block: BaseRenameRule) {
    renameRules[name] = block
}
val com.android.builder.model.BuildType.renameRule: BaseRenameRule get() =
    renameRules[name] ?: {
        when (flavorType) {
            "release" -> " V${versionName}(${versionCode})"
            else -> "_${versionName}_${versionCode}"
        }
    }