package com.example.githubjetpackcompose.data.service

import com.example.githubjetpackcompose.data.model.DetailsDepotDataJson
import com.example.githubjetpackcompose.data.model.ListReposDataItemJson
import com.example.githubjetpackcompose.data.model.UserDataJson
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitService {

    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): Response<UserDataJson>

    @GET("users/{username}/repos")
    suspend fun getUserRepositories(@Path("username") username: String): Response<List<ListReposDataItemJson>>

    @GET("repos/{username}/{repoName}")
    suspend fun getRepositoryDetails(
        @Path("username") username: String,
        @Path("repoName") repoName: String,
    ): Response<DetailsDepotDataJson>
}
