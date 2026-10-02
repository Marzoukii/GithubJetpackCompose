package com.example.githubjetpackcompose.domain.usecase

import com.example.githubjetpackcompose.data.NetworkResult
import com.example.githubjetpackcompose.data.repository.GitRepository
import com.example.githubjetpackcompose.domain.model.ListReposDataItemModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserRepositoriesUseCase @Inject constructor(
    private var gitRepository: GitRepository
) {
    fun execute(username: String): Flow<NetworkResult<List<ListReposDataItemModel>?>> = flow {
        gitRepository.getUserRepositories(username).collect {
            emit(it)
        }
    }
}
