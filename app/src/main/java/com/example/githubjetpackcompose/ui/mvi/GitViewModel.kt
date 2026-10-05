package com.example.githubjetpackcompose.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubjetpackcompose.data.NetworkResult
import com.example.githubjetpackcompose.domain.usecase.GetRepositoryDetailsUseCase
import com.example.githubjetpackcompose.domain.usecase.GetUserRepositoriesUseCase
import com.example.githubjetpackcompose.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GitViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getUserRepositoriesUseCase: GetUserRepositoriesUseCase,
    private val getRepositoryDetailsUseCase: GetRepositoryDetailsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GitState())
    val state: StateFlow<GitState> = _state.asStateFlow()

    fun handleIntent(intent: GitIntent) {
        when (intent) {
            is GitIntent.FetchUser -> fetchUser(intent.username)
            is GitIntent.FetchUserRepositories -> fetchUserRepositories(intent.username)
            is GitIntent.FetchRepositoryDetails -> fetchRepositoryDetails(intent.username, intent.repoName)
        }
    }

    private fun fetchUser(username: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getUserUseCase.execute(username).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _state.update { it.copy(isLoading = false, user = result.data) }
                    }
                    is NetworkResult.Error -> {
                        _state.update { it.copy(isLoading = false, error = result.exception.localizedMessage) }
                    }
                }
            }
        }
    }

    private fun fetchUserRepositories(username: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getUserRepositoriesUseCase.execute(username).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _state.update { it.copy(isLoading = false, repositories = result.data) }
                    }
                    is NetworkResult.Error -> {
                        _state.update { it.copy(isLoading = false, error = result.exception.localizedMessage) }
                    }
                }
            }
        }
    }

    private fun fetchRepositoryDetails(username: String, repoName: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getRepositoryDetailsUseCase.execute(username, repoName).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _state.update { it.copy(isLoading = false, repositoryDetails = result.data) }
                    }
                    is NetworkResult.Error -> {
                        _state.update { it.copy(isLoading = false, error = result.exception.localizedMessage) }
                    }
                }
            }
        }
    }
}
