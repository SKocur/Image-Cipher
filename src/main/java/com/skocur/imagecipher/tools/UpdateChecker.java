package com.skocur.imagecipher.tools;

import com.skocur.imagecipher.rest.di.DaggerServiceComponent;
import com.skocur.imagecipher.rest.di.ServiceComponent;
import com.skocur.imagecipher.rest.di.ServiceModule;
import com.skocur.imagecipher.rest.github.GitHubService;
import com.skocur.imagecipher.rest.github.Release;
import java.io.IOException;
import java.util.Date;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

      Date buildTime = ManifestReader.getBuildTime();
      if (buildTime == null) {
        System.out.println("Build-Time is null, probably manifest is missing");
        return;
      }

      if (buildTime.before(release.publishedDate)) {
        displayUpdateAlert();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void displayUpdateAlert() {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setHeaderText("New update is available to download");
    alert.showAndWait();
  }
}
