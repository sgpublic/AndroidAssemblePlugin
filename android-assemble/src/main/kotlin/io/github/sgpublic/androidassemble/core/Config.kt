package io.github.sgpublic.androidassemble.core

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ApplicationVariant
import org.gradle.api.Project
import java.io.File

interface AssembleOption {
    var outputDir: Any

    fun getOutputDir(): File {
        return when (outputDir) {
            is File -> outputDir as File
            is String -> File(outputDir as String)
            else -> throw RuntimeException("AssembleOption#outputDir can only be a File or String")
        }
    }
}

internal open class AssembleOptionImpl(project: Project):
    AssembleOption,
    Project by project {
    override var outputDir: Any = File(
        rootProject.projectDir,
        "./assemble/${project.name}"
    )
}


private val assembleOption = hashMapOf<String, AssembleOption>()
fun Project.assembleOption(block: AssembleOption.() -> Unit) {
    assembleOption[name] = AssembleOptionImpl(this).also(block)
}
fun Project.assembleOption(): AssembleOption {
    return assembleOption[name] ?: AssembleOptionImpl(this)
}

data class RenameParam(
    val flavorName: String,
    val buildType: String,
    val versionName: String,
    val versionCode: Int,
)


typealias BaseRenameRule = (RenameParam).() -> String

private val renameRules = hashMapOf<String, HashMap<String, BaseRenameRule>>()
internal fun Project.registerRenameRule(): HashMap<String, BaseRenameRule> {
    return hashMapOf<String, BaseRenameRule>().also {
        renameRules[name] = it
    }
}

fun com.android.build.api.dsl.BuildType.renameRule(project: Project, block: BaseRenameRule) {
    (renameRules[project.name] ?: project.registerRenameRule())[name] = block
}

internal fun Project.renameRule(buildType: String): BaseRenameRule {
    return renameRules[name]?.get(buildType) ?: rule@{
        return@rule if (buildType == "release") {
            "${rootProject.name}-${name} V${versionName}(${versionCode})"
        } else {
            "${rootProject.name}-${name}_${versionName}_${versionCode}"
        }
    }
}