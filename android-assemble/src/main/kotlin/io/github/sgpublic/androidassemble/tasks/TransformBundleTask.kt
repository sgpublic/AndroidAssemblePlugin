package io.github.sgpublic.androidassemble.tasks

import io.github.sgpublic.androidassemble.core.LocateArtifactsTask
import io.github.sgpublic.androidassemble.internal.FileArtifactProperty
import io.github.sgpublic.androidassemble.internal.file
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction

internal abstract class TransformBundleTask: LocateArtifactsTask(), FileArtifactProperty {
    final override fun targetExtension(): String = "aab"

    @get:InputFiles
    abstract override val fileProperty: RegularFileProperty

    @TaskAction
    open fun action() {
        doTransform()
    }
}