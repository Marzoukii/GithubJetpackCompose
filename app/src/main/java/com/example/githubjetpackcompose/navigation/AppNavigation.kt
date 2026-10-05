package com.example.githubjetpackcompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.githubjetpackcompose.ui.mvi.GitIntent
import com.example.githubjetpackcompose.ui.mvi.GitState
import com.example.githubjetpackcompose.ui.screen.RepoDetailScreen
import com.example.githubjetpackcompose.ui.screen.RepoListScreen
import com.example.githubjetpackcompose.ui.screen.UserScreen
import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
data class RepoList(val username: String)

@Serializable
data class RepoDetail(val username: String, val repoName: String)

@Composable
fun AppNavigation(
    state: GitState,
    onIntent: (GitIntent) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Login,
        modifier = modifier
    ) {
        composable<Login> {
            UserScreen(
                state = state,
                onIntent = onIntent,
                onNavigateToRepos = { username ->
                    onIntent(GitIntent.FetchUserRepositories(username))
                    navController.navigate(RepoList(username = username))
                }
            )
        }

        composable<RepoList> { backStackEntry ->
            val route: RepoList = backStackEntry.toRoute()
            RepoListScreen(
                state = state,
                onIntent = onIntent,
                initialUsername = route.username,
                onBack = { navController.popBackStack() },
                onRepoClick = { repoName ->
                    navController.navigate(RepoDetail(username = route.username, repoName = repoName))
                }
            )
        }

        composable<RepoDetail> { backStackEntry ->
            val route: RepoDetail = backStackEntry.toRoute()
            RepoDetailScreen(
                state = state,
                onIntent = onIntent,
                username = route.username,
                repoName = route.repoName,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
