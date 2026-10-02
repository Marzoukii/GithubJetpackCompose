package com.example.githubjetpackcompose.domain.usecase

import com.example.githubjetpackcompose.data.NetworkResult
import com.example.githubjetpackcompose.data.repository.GitRepository
import com.example.githubjetpackcompose.domain.model.ListReposDataItemModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserRepositoriesUseCase @Inject constructor(
    private val gitRepository: GitRepository
) {
    fun execute(username: String): Flow<NetworkResult<List<ListReposDataItemModel>?>> {
        return gitRepository.getUserRepositories(username)
    }
}
