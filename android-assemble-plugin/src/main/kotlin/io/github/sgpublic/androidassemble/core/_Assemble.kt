package io.github.sgpublic.androidassemble.core

import com.android.build.api.dsl.LibraryDefaultConfig
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ApplicationVariant
import com.android.build.api.variant.LibraryVariant
import io.github.sgpublic.androidassemble.internal.Loggable
import io.github.sgpublic.androidassemble.internal.setupDirArtifactPropertyFrom
import io.github.sgpublic.androidassemble.internal.setupFileArtifactPropertyFrom
import io.github.sgpublic.androidassemble.internal.setupManifestPropertyFrom
import io.github.sgpublic.androidassemble.tasks.LocateAarTask
import io.github.sgpublic.androidassemble.tasks.LocateApkTask
import io.github.sgpublic.androidassemble.tasks.LocateBundleTask
import org.gradle.api.Project

class ApplyAction internal constructor(project: Project):
    Loggable,
    Project by project {
    internal fun applyAssemble() {
        extensions.getByType(AndroidComponentsExtension::class.java).invoke()
    }

    private fun AndroidComponentsExtension<*, *, *>.invoke() {
        finalizeDsl {
            onVariants { variant ->
                val taskName = "${variant.flavorName?.capitalize() ?: ""}${variant.buildType?.capitalize() ?: ""}"
                when (variant) {
                    is ApplicationVariant -> {
                        tasks.register("assemble${taskName}AndLocate", LocateApkTask::class.java) {
                            setupDirArtifactPropertyFrom(variant)
                            setupVariantPropertyFrom(variant)
                            setupManifestPropertyFrom(variant)
                            dependsOn("assemble${taskName}")
                        }
                        tasks.register("bundle${taskName}AndLocate", LocateBundleTask::class.java) {
                            setupFileArtifactPropertyFrom(variant)
                            setupVariantPropertyFrom(variant)
                            setupManifestPropertyFrom(variant)
                            dependsOn("bundle${taskName}")
                        }
                    }
                    is LibraryVariant -> {
                        tasks.register("assemble${taskName}AndLocate", LocateAarTask::class.java) {
                            setupFileArtifactPropertyFrom(variant)
                            setupVariantPropertyFrom(variant, it.defaultConfig as LibraryDefaultConfig)
                            dependsOn("assemble${taskName}")
                        }
                    }
                }
            }
        }
    }
}