package io.github.sgpublic.androidassemble.internal

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryDefaultConfig
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.*
import com.sgpublic.xml.SXMLObject
import io.github.sgpublic.androidassemble.core.RenameParam
import io.github.sgpublic.androidassemble.core.assembleOption
import io.github.sgpublic.androidassemble.core.renameRule
import io.github.sgpublic.androidassemble.util.libVersionCode
import io.github.sgpublic.androidassemble.util.libVersionName
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import java.io.File
import java.io.Serializable

internal interface TargetFileProperty {
    fun targetExtension(): String
    fun target(): File

    fun getProject(): Project

    fun VariantProperty.doTransform(): File {
        if (this is ManifestProperty) {
            SXMLObject(manifestProperty.get().asFile.readText()).let {
                versionName.set(it.getStringAttr("android:versionName"))
                versionCode.set(it.getIntAttr("android:versionCode"))
            }
        }
        getProject().run {
            return target().copy(File(
                assembleOption().getOutputDir(),
                renameRule(buildType.get()).invoke(asRenameParam) + ".${targetExtension()}"
            ))
        }
    }


    fun VariantProperty.setupVariantPropertyFrom(variant: ApplicationVariant) {
        buildType.set(variant.buildType)
        flavorName.set(variant.flavorName)
    }

    fun VariantProperty.setupVariantPropertyFrom(variant: LibraryVariant, defaultConfig: LibraryDefaultConfig) {
        buildType.set(variant.buildType)
        flavorName.set(variant.flavorName)
        versionName.set(defaultConfig.libVersionName)
        versionCode.set(defaultConfig.libVersionCode)
    }
}

internal interface FileArtifactProperty: TargetFileProperty {
    val fileProperty: RegularFileProperty

    override fun target(): File {
        return fileProperty.get().asFile
    }
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

    override fun target(): File {
        builtArtifactsLoader.get().load(
            dirProperty.get()
        )?.elements?.let { elements ->
            for (element in elements) {
                return File(element.outputFile)
                    .takeIf { it.extension == targetExtension() }
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

internal interface ManifestProperty {
    val manifestProperty: RegularFileProperty
}
internal fun ManifestProperty.setupManifestPropertyFrom(variant: ApplicationVariant) {
    manifestProperty.set(variant.artifacts.get(SingleArtifact.MERGED_MANIFEST))
}