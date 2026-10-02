package com.example.githubjetpackcompose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubjetpackcompose.data.NetworkResult
import com.example.githubjetpackcompose.domain.model.DetailsDepotDataModel
import com.example.githubjetpackcompose.domain.model.ListReposDataItemModel
import com.example.githubjetpackcompose.domain.model.UserDataModel
import com.example.githubjetpackcompose.domain.usecase.GetRepositoryDetailsUseCase
import com.example.githubjetpackcompose.domain.usecase.GetUserRepositoriesUseCase
import com.example.githubjetpackcompose.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GitViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getUserRepositoriesUseCase: GetUserRepositoriesUseCase,
    private val getRepositoryDetailsUseCase: GetRepositoryDetailsUseCase
) : ViewModel() {

    private val _userState = MutableStateFlow<NetworkResult<UserDataModel?>?>(null)
    val userState: StateFlow<NetworkResult<UserDataModel?>?> = _userState.asStateFlow()

    private val _reposState = MutableStateFlow<NetworkResult<List<ListReposDataItemModel>?>?>(null)
    val reposState: StateFlow<NetworkResult<List<ListReposDataItemModel>?>?> = _reposState.asStateFlow()

    private val _repoDetailsState = MutableStateFlow<NetworkResult<DetailsDepotDataModel?>?>(null)
    val repoDetailsState: StateFlow<NetworkResult<DetailsDepotDataModel?>?> = _repoDetailsState.asStateFlow()

    fun fetchUser(username: String) {
        viewModelScope.launch {
            getUserUseCase.execute(username).collect { result ->
                _userState.value = result
            }
        }
    }

    fun fetchUserRepositories(username: String) {
        viewModelScope.launch {
            getUserRepositoriesUseCase.execute(username).collect { result ->
                _reposState.value = result
            }
        }
    }

    fun fetchRepositoryDetails(username: String, repoName: String) {
        viewModelScope.launch {
            getRepositoryDetailsUseCase.execute(username, repoName).collect { result ->
                _repoDetailsState.value = result
            }
        }
    }
}
