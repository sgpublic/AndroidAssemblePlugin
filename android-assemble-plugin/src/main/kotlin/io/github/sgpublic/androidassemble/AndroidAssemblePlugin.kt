package io.github.sgpublic.androidassemble

import io.github.sgpublic.androidassemble.core.applyAssemble
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger

class AndroidAssemblePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        _currentProject = project
        _logger = project.logger

        project.applyAssemble()
    }

    companion object {
        private lateinit var _currentProject: Project
        private lateinit var _logger: Logger

        val Project: Project get() = _currentProject
        val RootProject: Project get() = Project.rootProject
        val Logger: Logger get() = _logger
    }
}