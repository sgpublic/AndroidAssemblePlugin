package io.github.sgpublic.androidassemble.tasks

import io.github.sgpublic.androidassemble.internal.locate
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction

internal abstract class LocateBundleTask: TransformBundleTask() {
    @get:InputFiles
    abstract override val fileProperty: RegularFileProperty

    @TaskAction
    override fun action() {
        super.action()
        target.locate()
    }
}