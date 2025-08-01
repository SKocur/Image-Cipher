package com.imagecipher.app.rest.github

import retrofit2.Call
import retrofit2.http.GET

interface GitHubService {

    @GET("repos/SKocur/Image-Cipher/releases/latest")
    fun getLatestRelease(): Call<Release>
}
