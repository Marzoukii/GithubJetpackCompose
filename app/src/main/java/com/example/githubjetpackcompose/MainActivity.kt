package com.example.githubjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.githubjetpackcompose.ui.mvi.GitIntent
import com.example.githubjetpackcompose.ui.mvi.GitViewModel
import com.example.githubjetpackcompose.ui.screen.RepoListScreen
import com.example.githubjetpackcompose.ui.screen.UserScreen
import com.example.githubjetpackcompose.ui.theme.GithubJetpackComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubJetpackComposeTheme {
                GitApp()
            }
        }
    }
}

@Composable
fun GitApp(viewModel: GitViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var currentUsername by remember { mutableStateOf("octocat") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    label = { Text("User Profile") },
                    icon = { Text("👤") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        if (state.repositories == null) {
                            viewModel.handleIntent(GitIntent.FetchUserRepositories(currentUsername))
                        }
                    },
                    label = { Text("Repositories") },
                    icon = { Text("📁") }
                )
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            0 -> UserScreen(
                state = state,
                onIntent = { viewModel.handleIntent(it) },
                onNavigateToRepos = { username ->
                    currentUsername = username
                    viewModel.handleIntent(GitIntent.FetchUserRepositories(username))
                    selectedTab = 1
                },
                modifier = Modifier.padding(innerPadding)
            )
            1 -> RepoListScreen(
                state = state,
                onIntent = { viewModel.handleIntent(it) },
                initialUsername = currentUsername,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
