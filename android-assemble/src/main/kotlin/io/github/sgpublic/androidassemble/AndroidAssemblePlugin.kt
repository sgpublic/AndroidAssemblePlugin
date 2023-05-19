package io.github.sgpublic.androidassemble

import com.android.build.api.variant.ApplicationVariant
import com.android.build.api.variant.LibraryVariant
import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.android.build.gradle.api.VersionedVariant
import io.github.sgpublic.androidassemble.core.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.internal.os.OperatingSystem
import java.io.File

class AndroidAssemblePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        _logger = project.logger
        _rootProject = project.rootProject
        ApplyAction(project).applyAssemble()
    }

    companion object {
        private lateinit var _rootProject: Project
        private lateinit var _logger: Logger

        val RootProject: Project get() = _rootProject
        val Logger: Logger get() = _logger
    }
}
