package org.octopusden.octopus.artifactory.npm.maven.plugin.service

import org.octopusden.octopus.artifactory.npm.maven.plugin.configuration.ArtifactoryConfiguration
import org.octopusden.octopus.artifactory.npm.maven.plugin.configuration.PluginConfiguration

interface NpmBuildInfoIntegrationService {
    fun generateNpmBuildInfo(pluginConfig: PluginConfiguration, artifactoryConfig: ArtifactoryConfiguration)
    fun integrateNpmBuildInfo(pluginConfig: PluginConfiguration)
}