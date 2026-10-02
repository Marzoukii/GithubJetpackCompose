package com.example.githubjetpackcompose.domain.usecase

import com.example.githubjetpackcompose.data.NetworkResult
import com.example.githubjetpackcompose.data.repository.GitRepository
import com.example.githubjetpackcompose.domain.model.DetailsDepotDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRepositoryDetailsUseCase @Inject constructor(
    private var gitRepository: GitRepository
) {
    fun execute(username: String, repoName: String): Flow<NetworkResult<DetailsDepotDataModel?>> = flow {
        gitRepository.getRepositoryDetails(username, repoName).collect {
            emit(it)
        }
    }
}
