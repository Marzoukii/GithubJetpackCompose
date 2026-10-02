package com.example.githubjetpackcompose.domain.usecase

import com.example.githubjetpackcompose.data.NetworkResult
import com.example.githubjetpackcompose.data.repository.GitRepository
import com.example.githubjetpackcompose.domain.model.DetailsDepotDataModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRepositoryDetailsUseCase @Inject constructor(
    private val gitRepository: GitRepository
) {
    fun execute(username: String, repoName: String): Flow<NetworkResult<DetailsDepotDataModel?>> {
        return gitRepository.getRepositoryDetails(username, repoName)
    }
}
