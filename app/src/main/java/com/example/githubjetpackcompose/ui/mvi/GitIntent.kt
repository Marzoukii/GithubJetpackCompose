package com.example.githubjetpackcompose.ui.mvi

sealed class GitIntent {
    data class FetchUser(val username: String) : GitIntent()
    data class FetchUserRepositories(val username: String) : GitIntent()
    data class FetchRepositoryDetails(val username: String, val repoName: String) : GitIntent()
}
