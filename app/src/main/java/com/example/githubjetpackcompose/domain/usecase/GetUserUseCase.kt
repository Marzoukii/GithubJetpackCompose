package com.example.githubjetpackcompose.domain.usecase

import com.example.githubjetpackcompose.data.NetworkResult
import com.example.githubjetpackcompose.data.repository.GitRepository
import com.example.githubjetpackcompose.domain.model.UserDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private var gitRepository: GitRepository
) {
    fun execute(username: String): Flow<NetworkResult<UserDataModel?>> = flow {
        gitRepository.getUser(username).collect {
            emit(it)
        }
    }
}
