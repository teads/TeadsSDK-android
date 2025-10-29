package tv.teads

import org.gradle.api.Project

/**
 * Gradle project extensions for version management.
 * These extensions provide convenient access to version properties defined in gradle.properties.
 */

/**
 * Gets the version code as an integer from gradle.properties
 */
val Project.versionCode: Int
    get() = intProperty("VERSION_CODE")

/**
 * Gets the version name as a string from gradle.properties
 */
val Project.versionName: String
    get() = stringProperty("VERSION_NAME")

private fun Project.stringProperty(name: String): String {
    return property(name) as String
}

private fun Project.intProperty(name: String): Int {
    return (property(name) as String).toInt()
}
