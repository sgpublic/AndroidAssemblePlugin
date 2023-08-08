package io.github.sgpublic.androidassemble

import io.github.sgpublic.androidassemble.core.ApplyAction
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidAssemblePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        ApplyAction(project).applyAssemble()
    }
}
