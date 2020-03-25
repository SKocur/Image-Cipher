package com.skocur.imagecipher.rest.di;

import com.skocur.imagecipher.rest.github.GitHubService;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ServiceModule {

  @Provides
  @Singleton
  @Named("GitHubService")
  public GitHubService provideGitHubService() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    return retrofit.create(GitHubService.class);
  }

}
