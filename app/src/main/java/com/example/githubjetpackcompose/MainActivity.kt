package com.example.githubjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.githubjetpackcompose.navigation.AppNavigation
import com.example.githubjetpackcompose.ui.mvi.GitViewModel
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

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        AppNavigation(
            state = state,
            onIntent = { viewModel.handleIntent(it) },
            modifier = Modifier.padding(innerPadding)
        )
    }
}
