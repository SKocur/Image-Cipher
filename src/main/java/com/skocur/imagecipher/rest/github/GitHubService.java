package com.skocur.imagecipher.rest.github;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GitHubService {

  @GET("repos/SKocur/Image-Cipher/releases/latest")
  Call<Release> getLatestRelease();
}
