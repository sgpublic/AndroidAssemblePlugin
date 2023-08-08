package io.github.sgpublic.androidassemble.internal

import org.gradle.api.logging.Logging
import org.slf4j.Logger

internal interface Loggable

internal val Loggable.log: Logger get() = Logging.getLogger(javaClass)