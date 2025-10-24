package org.octopusden.octopus.artifactory.npm.maven.plugin.configuration

data class PluginConfiguration(
    val buildName: String,
    val buildNumber: String,
    val npmBuildNameSuffix: String,
    val npmRepository: String,
    val workingDirectory: String,
    val skipExecution: Boolean,
    val cleanupNpmBuildInfo: Boolean
) {
    val npmBuildName: String
        get() = "${buildName}${npmBuildNameSuffix}"
}