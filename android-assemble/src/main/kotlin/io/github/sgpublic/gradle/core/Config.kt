package io.github.sgpublic.gradle.core

import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.internal.api.ApkVariantImpl
import com.android.build.gradle.internal.api.ApkVariantOutputImpl
import com.android.build.gradle.internal.api.LibraryVariantImpl
import com.android.build.gradle.internal.api.LibraryVariantOutputImpl
import io.github.sgpublic.gradle.AndroidAssemblePlugin
import org.gradle.api.Project
import java.io.File

interface AssembleOption {
    var apkOutputDir: File
    var aarOutputDir: File

    fun getOutputDir(variant: BaseVariant): File? {
        return when (variant) {
            is ApkVariantOutputImpl -> apkOutputDir
            is LibraryVariantOutputImpl -> aarOutputDir
            else -> null
        }
    }
}

internal open class AssembleOptionImpl: AssembleOption {
    private var _apkOutputDir: File = AndroidAssemblePlugin.rootProject.file("./build/assemble/apk")
    override var apkOutputDir: File
        get() = _apkOutputDir
        set(value) { _apkOutputDir = value }

    private var _aarOutputDir: File = AndroidAssemblePlugin.rootProject.file("./build/assemble/aar")
    override var aarOutputDir: File
        get() = _aarOutputDir
        set(value) { _aarOutputDir = value }
}

internal object DefaultAssembleOption: AssembleOptionImpl()

internal val assembleOption = hashMapOf<Project, AssembleOption>()
fun Project.assembleOption(block: (AssembleOption) -> Unit) {
    assembleOption[this] = AssembleOptionImpl().also(block)
}

data class RenameParam(
    val projectName: String,
    val flavorType: String,
    val buildType: String,
    val versionName: String,
    val versionCode: Int = 1,
)


typealias BaseRenameRule = BaseVariant.() -> String

private val renameRules = hashMapOf<String, BaseRenameRule>()
fun com.android.build.api.dsl.BuildType.renameRule(block: BaseRenameRule) {
    renameRules[name] = block
}
val com.android.builder.model.BuildType.renameRule: BaseRenameRule get() = renameRules[name]!!