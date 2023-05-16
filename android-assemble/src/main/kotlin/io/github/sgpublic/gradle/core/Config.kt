package io.github.sgpublic.gradle.core

import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.LibraryBuildType
import com.android.build.gradle.TestedExtension
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.internal.api.ApkVariantOutputImpl
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

internal class AssembleOptionImpl: AssembleOption {
    private var _apkOutputDir: File = AndroidAssemblePlugin.rootProject.file("./build/assemble/apk")
    override var apkOutputDir: File
        get() = _apkOutputDir
        set(value) { _apkOutputDir = value }

    private var _aarOutputDir: File = AndroidAssemblePlugin.rootProject.file("./build/assemble/aar")
    override var aarOutputDir: File
        get() = _aarOutputDir
        set(value) { _aarOutputDir = value }
}

internal val assembleOption = hashMapOf<Project, AssembleOption>()
fun Project.assembleOption(block: (AssembleOption) -> Unit) {
    assembleOption[this] = AssembleOptionImpl().also(block)
}

data class AppRenameParam(
    val projectName: String,
    val flavorType: String,
    val buildType: String,
    val versionName: String,
    val versionCode: Int,
)
typealias AppRenameRule = AppRenameParam.() -> String
private val appRenameRule = hashMapOf<ApplicationBuildType, AppRenameRule>()
fun ApplicationBuildType.renameRule(block: AppRenameRule) {
    appRenameRule[this] = block
}

data class LibRenameParam(
    val projectName: String,
    val flavorType: String,
    val buildType: String,
    val versionName: String,
)
typealias LibRenameRule = LibRenameParam.() -> String
private val libRenameRule = hashMapOf<LibraryBuildType, LibRenameRule>()
fun LibraryBuildType.renameRule(block: LibRenameRule) {
    libRenameRule[this] = block
}
val LibraryBuildType.renameRule: LibRenameRule = 