package com.example.githubjetpackcompose.data.repository

import com.example.githubjetpackcompose.data.NetworkResult
import com.example.githubjetpackcompose.data.service.GitService
import com.example.githubjetpackcompose.domain.mapper.toDomain
import com.example.githubjetpackcompose.domain.model.UserDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GitRepository @Inject constructor(
    private val gitService: GitService
) {
    fun getUser(username: String): Flow<NetworkResult<UserDataModel>> = flow {
        try {
            val response = gitService.getUser(username)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    emit(NetworkResult.Success(body.toDomain()))
                } else {
                    emit(NetworkResult.Error(Exception("Response body is null")))
                }
            } else {
                emit(NetworkResult.Error(Exception("Error code: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e))
        }
    }
}
