package org.octopusden.octopus.artifactory.npm.maven.plugin.service

import org.octopusden.octopus.artifactory.npm.maven.plugin.configuration.ArtifactoryConfiguration

interface JFrogNpmCliService {
    fun configureNpmRepository(packageJsonPath: String, npmRepository: String)
    fun installNpmDependencies(packageJsonPath: String, buildName: String, buildNumber: String)
    fun publishNpmBuildInfo(packageJsonPath: String, buildName: String, buildNumber: String, artifactoryConfig: ArtifactoryConfiguration)
    fun isJFrogCliAvailable(): Boolean
}