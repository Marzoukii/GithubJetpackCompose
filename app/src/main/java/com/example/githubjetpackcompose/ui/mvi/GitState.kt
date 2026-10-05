package com.example.githubjetpackcompose.ui.mvi

import com.example.githubjetpackcompose.domain.model.DetailsDepotDataModel
import com.example.githubjetpackcompose.domain.model.ListReposDataItemModel
import com.example.githubjetpackcompose.domain.model.UserDataModel

data class GitState(
    val isLoading: Boolean = false,
    val user: UserDataModel? = null,
    val repositories: List<ListReposDataItemModel>? = null,
    val repositoryDetails: DetailsDepotDataModel? = null,
    val error: String? = null
)
