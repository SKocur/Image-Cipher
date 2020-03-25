package com.skocur.imagecipher.tools;

import com.skocur.imagecipher.rest.di.DaggerServiceComponent;
import com.skocur.imagecipher.rest.di.ServiceComponent;
import com.skocur.imagecipher.rest.di.ServiceModule;
import com.skocur.imagecipher.rest.github.GitHubService;
import com.skocur.imagecipher.rest.github.Release;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Named;
import retrofit2.Call;
import retrofit2.Response;

public class UpdateChecker {

  @Inject
  @Named("GitHubService")
  public GitHubService gitHubService;

  public UpdateChecker() {
    ServiceComponent serviceComponent = DaggerServiceComponent.builder()
        .serviceModule(new ServiceModule()).build();
    serviceComponent.injectUpdater(this);
  }

  public void checkForUpdates() {
    Call<Release> releaseCall = gitHubService.getLatestRelease();
    try {
      Response<Release> response = releaseCall.execute();
      Release release = response.body();

      if (release == null) {
        System.err.println("Release response body is null");
        return;
      }

      //TODO: Compare dates and display alert if newer release is present
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
