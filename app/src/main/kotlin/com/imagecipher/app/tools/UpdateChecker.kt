package com.imagecipher.app.tools

import com.imagecipher.app.rest.github.GitHubService
import com.imagecipher.app.rest.github.Release
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.function.Supplier

class UpdateChecker {
    private val gitHubService: GitHubService = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GitHubService::class.java)

    init {
        logger.info("Initializing GitHub Service")
    }

    /**
     * @return Boolean - status whether app update was processed or not
     */
    fun checkForUpdates(supplier: Supplier<Boolean?>): Boolean {
        logger.info("Checking for updates")
        val releaseCall: Call<Release> = gitHubService.getLatestRelease()
        try {
            val response = releaseCall.execute()
            val release = response.body()

            if (release == null) {
                logger.error("Release response body is null")
                return false
            }

            val buildTime = ManifestReader.getBuildTime()
            if (buildTime == null) {
                logger.warn("Build-Time is null, probably manifest is missing")
                return false
            }

            if (buildTime.before(release.publishedDate)) {
                return supplier.get()!!
            } else {
                logger.debug("No new version is available. Current build date: " + buildTime.toString())
            }
        } catch (e: IOException) {
            logger.error(e)
        }

        return false
    }

    companion object {
        private val logger: Logger = LogManager.getLogger()
    }
}
