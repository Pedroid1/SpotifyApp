package com.pedroid.convention

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project

/**
 * Configures the detekt plugin.
 */
internal fun Project.configureDetekt(extension: DetektExtension) {
    extension.apply {
        config.from(rootProject.files("app/config/detekt/config.yml"))
        parallel = true
        buildUponDefaultConfig = true
        autoCorrect = true
    }
}
