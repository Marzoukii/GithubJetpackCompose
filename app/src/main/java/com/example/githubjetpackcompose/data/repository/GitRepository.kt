package com.example.githubjetpackcompose.data.repository

import com.example.githubjetpackcompose.data.NetworkResult
import com.example.githubjetpackcompose.data.service.GitService
import com.example.githubjetpackcompose.domain.mapper.GitMapper
import com.example.githubjetpackcompose.domain.model.DetailsDepotDataModel
import com.example.githubjetpackcompose.domain.model.ListReposDataItemModel
import com.example.githubjetpackcompose.domain.model.UserDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GitRepository @Inject constructor(
    private val gitService: GitService,
    private val gitMapper: GitMapper
) {
    fun getUser(username: String): Flow<NetworkResult<UserDataModel?>> = flow {
        try {
            val response = gitService.getUser(username)
            if (response.isSuccessful) {
                emit(NetworkResult.Success(gitMapper.toUserDataModel(response.body())))
            } else {
                emit(NetworkResult.Error(Exception("Error: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e))
        }
    }

    fun getUserRepositories(username: String): Flow<NetworkResult<List<ListReposDataItemModel>?>> = flow {
        try {
            val response = gitService.getUserRepositories(username)
            if (response.isSuccessful) {
                emit(NetworkResult.Success(gitMapper.toListReposDataItemModel(response.body())))
            } else {
                emit(NetworkResult.Error(Exception("Error: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e))
        }
    }

    fun getRepositoryDetails(username: String, repoName: String): Flow<NetworkResult<DetailsDepotDataModel?>> = flow {
        try {
            val response = gitService.getRepositoryDetails(username, repoName)
            if (response.isSuccessful) {
                emit(NetworkResult.Success(gitMapper.toDetailsDepotDataModel(response.body())))
            } else {
                emit(NetworkResult.Error(Exception("Error: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e))
        }
    }
}
