package io.github.sgpublic.androidassemble.internal

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.*
import io.github.sgpublic.androidassemble.core.RenameParam
import io.github.sgpublic.androidassemble.core.assembleOption
import io.github.sgpublic.androidassemble.core.renameRule
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import java.io.File

internal interface TargetFileProperty {
    val targetExtension: String
    val target: File

    fun getProject(): Project

    fun VariantProperty.doTransform() {
        getProject().run {
            target.copy(File(
                assembleOption().getOutputDir(),
                renameRule(buildType.get()).invoke(asRenameParam) + ".$targetExtension"
            ))
        }
    }
}

internal interface FileArtifactProperty: TargetFileProperty {
    val fileProperty: RegularFileProperty

    override val target: File get() = fileProperty.get().asFile
}

internal fun FileArtifactProperty.setupFileArtifactPropertyFrom(variant: ApplicationVariant) {
    fileProperty.set(variant.artifacts.get(SingleArtifact.BUNDLE))
}
internal fun FileArtifactProperty.setupFileArtifactPropertyFrom(variant: LibraryVariant) {
    fileProperty.set(variant.artifacts.get(SingleArtifact.AAR))
}

internal interface DirArtifactProperty: TargetFileProperty {
    val dirProperty: DirectoryProperty
    val builtArtifactsLoader: Property<BuiltArtifactsLoader>

    override val target: File get() {
        builtArtifactsLoader.get().load(
            dirProperty.get()
        )?.elements?.let { elements ->
            for (element in elements) {
                return File(element.outputFile)
                    .takeIf { it.extension == targetExtension }
                    ?: continue
            }
        }
        throw RuntimeException("Cannot locate the apk file")
    }
}

internal fun DirArtifactProperty.setupDirArtifactPropertyFrom(variant: ApplicationVariant) {
    dirProperty.set(variant.artifacts.get(SingleArtifact.APK))
    builtArtifactsLoader.set(variant.artifacts.getBuiltArtifactsLoader())
}

internal val DirArtifactProperty.file: File? get() {
    val elements = builtArtifactsLoader.get().load(
        dirProperty.get()
    )?.elements ?: return null
    for (element in elements) {
        return File(element.outputFile)
            .takeIf { it.extension == "apk" }
            ?: continue
    }
    return null
}

internal interface VariantProperty {
    val buildType: Property<String>
    val flavorName: Property<String>
    val versionName: Property<String>
    val versionCode: Property<Int>
}

internal val VariantProperty.asRenameParam: RenameParam get() {
    return RenameParam(
        buildType = buildType.get(),
        flavorName = flavorName.get(),
        versionName = versionName.get(),
        versionCode = versionCode.get(),
    )
}

internal fun VariantProperty.setupVariantPropertyFrom(variant: Variant) {
    buildType.set(variant.buildType)
    flavorName.set(variant.flavorName)
}