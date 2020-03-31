package com.skocur.imagecipher.tools;

import com.skocur.imagecipher.rest.di.DaggerServiceComponent;
import com.skocur.imagecipher.rest.di.ServiceComponent;
import com.skocur.imagecipher.rest.di.ServiceModule;
import com.skocur.imagecipher.rest.github.GitHubService;
import com.skocur.imagecipher.rest.github.Release;
import java.io.IOException;
import java.util.Date;
import java.util.function.Supplier;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Call;
import retrofit2.Response;

public class UpdateChecker {

  @Inject
  @Named("GitHubService")
  public GitHubService gitHubService;

  private static final Logger logger = LogManager.getLogger();

  public UpdateChecker() {
    logger.info("Injecting GitHub Service");
    ServiceComponent serviceComponent = DaggerServiceComponent.builder()
        .serviceModule(new ServiceModule()).build();
    serviceComponent.injectUpdater(this);
  }

  /**
   * @return Boolean - status whether app update was processed or not
   */
  public boolean checkForUpdates(Supplier<Boolean> supplier) {
    logger.info("Checking for updates");
    Call<Release> releaseCall = gitHubService.getLatestRelease();
    try {
      Response<Release> response = releaseCall.execute();
      Release release = response.body();

      if (release == null) {
        logger.error("Release response body is null");
        return false;
      }

      Date buildTime = ManifestReader.getBuildTime();
      if (buildTime == null) {
        logger.warn("Build-Time is null, probably manifest is missing");
        return false;
      }

      if (buildTime.before(release.publishedDate)) {
        return supplier.get();
      } else {
        logger.debug("No new version is available. Current build date: " + buildTime.toString());
      }
    } catch (IOException e) {
      logger.error(e);
    }

    return false;
  }
}
