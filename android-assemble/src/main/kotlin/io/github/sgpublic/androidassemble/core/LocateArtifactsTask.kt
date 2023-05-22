package io.github.sgpublic.androidassemble.core

import io.github.sgpublic.androidassemble.internal.Loggable
import io.github.sgpublic.androidassemble.internal.VariantProperty
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal

abstract class LocateArtifactsTask: DefaultTask(), Loggable, VariantProperty {
    @get:Internal
    abstract override val buildType: Property<String>
    @get:Internal
    abstract override val flavorName: Property<String>
}