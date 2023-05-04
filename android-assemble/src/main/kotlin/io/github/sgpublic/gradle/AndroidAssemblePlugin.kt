package io.github.sgpublic.gradle

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import io.github.sgpublic.gradle.util.applyAssemble
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger

class AndroidAssemblePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        rootProject = project.rootProject
        logger = project.logger

        project.applyAssemble()
    }

    companion object {
        lateinit var rootProject: Project private set
        lateinit var logger: Logger private set
    }
}