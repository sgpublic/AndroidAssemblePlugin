package io.github.sgpublic.androidassemble.core

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.*
import io.github.sgpublic.androidassemble.internal.Loggable
import io.github.sgpublic.androidassemble.internal.setupDirArtifactPropertyFrom
import io.github.sgpublic.androidassemble.internal.setupFileArtifactPropertyFrom
import io.github.sgpublic.androidassemble.internal.setupVariantPropertyFrom
import io.github.sgpublic.androidassemble.tasks.LocateAarTask
import io.github.sgpublic.androidassemble.tasks.LocateApkTask
import io.github.sgpublic.androidassemble.tasks.LocateBundleTask
import org.gradle.api.Project
import org.gradle.api.provider.MapProperty
import java.io.Serializable

class ApplyAction internal constructor(project: Project):
    Loggable,
    Project by project {
    private val androidComponents: AndroidComponentsExtension<*, *, *> by lazy {
        extensions.getByType(AndroidComponentsExtension::class.java)
    }

    internal fun applyAssemble() {
        androidComponents.invoke()
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
                            dependsOn("assemble${taskName}")
                        }
                        tasks.register("bundle${taskName}AndLocate", LocateBundleTask::class.java) {
                            setupFileArtifactPropertyFrom(variant)
                            setupVariantPropertyFrom(variant)
                            dependsOn("bundle${taskName}")
                        }
                    }
                    is LibraryVariant -> {
                        tasks.register("assemble${taskName}AndLocate", LocateAarTask::class.java) {
                            setupFileArtifactPropertyFrom(variant)
                            setupVariantPropertyFrom(variant)
                            dependsOn("assemble${taskName}")
                        }
                    }
                }
            }
        }
    }
}

operator fun MapProperty<String, BuildConfigField<out Serializable>>.get(key: String) =
    getting(key).orNull?.value