package com.example.githubjetpackcompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.githubjetpackcompose.ui.mvi.GitIntent
import com.example.githubjetpackcompose.ui.mvi.GitState
import com.example.githubjetpackcompose.ui.screen.RepoListScreen
import com.example.githubjetpackcompose.ui.screen.UserScreen

sealed class Screen(val route: String) {
    object User : Screen("user_screen")
    object RepoList : Screen("repo_list_screen/{username}") {
        fun createRoute(username: String) = "repo_list_screen/$username"
    }
}

@Composable
fun AppNavigation(
    state: GitState,
    onIntent: (GitIntent) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.User.route,
        modifier = modifier
    ) {
        composable(route = Screen.User.route) {
            UserScreen(
                state = state,
                onIntent = onIntent,
                onNavigateToRepos = { username ->
                    onIntent(GitIntent.FetchUserRepositories(username))
                    navController.navigate(Screen.RepoList.createRoute(username))
                }
            )
        }

        composable(
            route = Screen.RepoList.route,
            arguments = listOf(
                navArgument("username") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "octocat"
            RepoListScreen(
                state = state,
                onIntent = onIntent,
                initialUsername = username
            )
        }
    }
}
