package org.octopusden.octopus.artifactory.npm.maven.plugin.service.impl

import org.apache.maven.plugin.MojoExecutionException
import org.octopusden.octopus.artifactory.npm.maven.plugin.configuration.ArtifactoryConfiguration
import org.octopusden.octopus.artifactory.npm.maven.plugin.configuration.PluginConfiguration
import org.octopusden.octopus.artifactory.npm.maven.plugin.service.ArtifactoryBuildInfoService
import org.octopusden.octopus.artifactory.npm.maven.plugin.service.JFrogNpmCliService
import org.octopusden.octopus.artifactory.npm.maven.plugin.service.NpmBuildInfoIntegrationService
import org.slf4j.LoggerFactory

class NpmBuildInfoIntegrationServiceImpl(
    private val jfrogNpmCliService: JFrogNpmCliService,
    private val buildInfoService: ArtifactoryBuildInfoService
) : NpmBuildInfoIntegrationService {
    
    private val logger = LoggerFactory.getLogger(NpmBuildInfoIntegrationServiceImpl::class.java)

    override fun generateNpmBuildInfo(
        pluginConfig: PluginConfiguration,
        artifactoryConfig: ArtifactoryConfiguration
    ) {
        logger.info("Generate NPM build info for build ${pluginConfig.buildName}:${pluginConfig.buildNumber}")

        if (!jfrogNpmCliService.isJFrogCliAvailable()) {
            throw MojoExecutionException("JFrog CLI is not available")
        }

        jfrogNpmCliService.configureNpmRepository(pluginConfig.workingDirectory, pluginConfig.npmRepository)
        jfrogNpmCliService.installNpmDependencies(pluginConfig.workingDirectory, pluginConfig.npmBuildName, pluginConfig.buildNumber)
        jfrogNpmCliService.publishNpmBuildInfo(pluginConfig.workingDirectory, pluginConfig.npmBuildName, pluginConfig.buildNumber, artifactoryConfig)
    }

    override fun integrateNpmBuildInfo(pluginConfig: PluginConfiguration) {
        try {
            logger.info("Integrate NPM build info into Maven build info for build ${pluginConfig.buildName}:${pluginConfig.buildNumber}")

            val mavenBuildInfo = buildInfoService.getBuildInfo(pluginConfig.buildName, pluginConfig.buildNumber)
            val npmBuildInfo = buildInfoService.getBuildInfo(pluginConfig.npmBuildName, pluginConfig.buildNumber)
            val mergedBuildInfo = buildInfoService.mergeBuildInfo(mavenBuildInfo, npmBuildInfo)
            buildInfoService.uploadBuildInfo(mergedBuildInfo)

            if (pluginConfig.cleanupNpmBuildInfo) {
                buildInfoService.deleteBuildInfo(pluginConfig.npmBuildName, listOf(pluginConfig.buildNumber))
            }
            
            logger.info("NPM build info integration completed successfully!")
        } catch (e: Exception) {
            val errorMessage = "Unexpected error during NPM build info integration: ${e.message}"
            logger.error(errorMessage, e)
            throw MojoExecutionException(errorMessage, e)
        }
    }

}