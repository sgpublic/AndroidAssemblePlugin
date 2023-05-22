package io.github.sgpublic.androidassemble.tasks

import com.android.build.api.variant.BuiltArtifactsLoader
import io.github.sgpublic.androidassemble.core.LocateArtifactsTask
import io.github.sgpublic.androidassemble.internal.DirArtifactProperty
import io.github.sgpublic.androidassemble.internal.locate
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

internal abstract class LocateApkTask: TransformApkTask() {
    @get:InputFiles
    abstract override val dirProperty: DirectoryProperty

    @get:Internal
    abstract override val builtArtifactsLoader: Property<BuiltArtifactsLoader>

    @TaskAction
    override fun action() {
        super.action()
        target.locate()
    }
}