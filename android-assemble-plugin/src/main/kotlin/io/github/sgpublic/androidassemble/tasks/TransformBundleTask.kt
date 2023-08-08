package io.github.sgpublic.androidassemble.tasks

import io.github.sgpublic.androidassemble.core.LocateArtifactsTask
import io.github.sgpublic.androidassemble.internal.FileArtifactProperty
import io.github.sgpublic.androidassemble.internal.ManifestProperty
import io.github.sgpublic.androidassemble.internal.file
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

internal abstract class TransformBundleTask: LocateArtifactsTask(), FileArtifactProperty, ManifestProperty {
    final override fun targetExtension(): String = "aab"

    @get:InputFiles
    abstract override val fileProperty: RegularFileProperty

    @get:InputFiles
    abstract override val manifestProperty: RegularFileProperty

    @get:Internal
    abstract override val buildType: Property<String>
    @get:Internal
    abstract override val flavorName: Property<String>
    @get:Internal
    abstract override val versionName: Property<String>
    @get:Internal
    abstract override val versionCode: Property<Int>

    @TaskAction
    open fun action() {
        doTransform()
    }
}