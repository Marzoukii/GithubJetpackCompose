package com.example.githubjetpackcompose.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubjetpackcompose.R
import com.example.githubjetpackcompose.domain.model.ListReposDataItemModel
import com.example.githubjetpackcompose.domain.model.OwnerModel
import com.example.githubjetpackcompose.ui.mvi.GitIntent
import com.example.githubjetpackcompose.ui.mvi.GitState
import com.example.githubjetpackcompose.ui.theme.GithubJetpackComposeTheme

private val CardBgBlue = Color(0xFF808DF5)
private val BorderPurple = Color(0xFF6E7CF1)

@Composable
fun RepoListScreen(
    state: GitState,
    onIntent: (GitIntent) -> Unit,
    modifier: Modifier = Modifier,
    initialUsername: String = "octocat"
) {
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(initialUsername) {
        if (state.repositories == null && initialUsername.isNotBlank()) {
            onIntent(GitIntent.FetchUserRepositories(initialUsername.trim()))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Input Bar matching screenshot
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text(
                    text = "Search",
                    color = Color.Gray.copy(alpha = 0.6f)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BorderPurple,
                unfocusedBorderColor = BorderPurple,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BorderPurple)
            }
        } else if (state.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: ${state.error}",
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            val allRepos = state.repositories ?: emptyList()
            val filteredRepos = remember(searchQuery, allRepos) {
                if (searchQuery.isBlank()) {
                    allRepos
                } else {
                    allRepos.filter { it.name.contains(searchQuery, ignoreCase = true) }
                }
            }

            if (filteredRepos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (allRepos.isEmpty()) "No repositories found" else "No matching repositories",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(filteredRepos, key = { it.id }) { repo ->
                        RepoGridTile(repo = repo)
                    }
                }
            }
        }
    }
}

@Composable
fun RepoGridTile(
    repo: ListReposDataItemModel,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBgBlue),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.repos),
                contentDescription = "Repo Icon",
                modifier = Modifier.size(54.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = repo.name,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RepoListScreenPreview() {
    val sampleOwner = OwnerModel(
        avatar_url = "",
        events_url = "",
        followers_url = "",
        following_url = "",
        gists_url = "",
        gravatar_id = "",
        html_url = "",
        id = 1,
        login = "octocat",
        node_id = "",
        organizations_url = "",
        received_events_url = "",
        repos_url = "",
        site_admin = false,
        starred_url = "",
        subscriptions_url = "",
        type = "User",
        url = ""
    )
    val mockRepos = listOf(
        ListReposDataItemModel(
            id = 1, name = "Test", allow_forking = true, archive_url = "", archived = false, assignees_url = "", blobs_url = "", branches_url = "", clone_url = "", collaborators_url = "", comments_url = "", commits_url = "", compare_url = "", contents_url = "", contributors_url = "", created_at = "", default_branch = "main", deployments_url = "", description = null, disabled = false, downloads_url = "", events_url = "", fork = false, forks = 0, forks_count = 0, forks_url = "", full_name = "octocat/Test", git_commits_url = "", git_refs_url = "", git_tags_url = "", git_url = "", has_discussions = false, has_downloads = true, has_issues = true, has_pages = false, has_projects = true, has_wiki = true, homepage = null, hooks_url = "", html_url = "", is_template = false, issue_comment_url = "", issue_events_url = "", issues_url = "", keys_url = "", labels_url = "", language = "Kotlin", languages_url = "", license = null, merges_url = "", milestones_url = "", mirror_url = null, node_id = "", notifications_url = "", open_issues = 0, open_issues_count = 0, owner = sampleOwner, private = false, pulls_url = "", pushed_at = "", releases_url = "", size = 100, ssh_url = "", stargazers_count = 5, stargazers_url = "", statuses_url = "", subscribers_url = "", subscription_url = "", svn_url = "", tags_url = "", teams_url = "", topics = emptyList(), trees_url = "", updated_at = "", url = "", visibility = "public", watchers = 0, watchers_count = 0, web_commit_signoff_required = false
        ),
        ListReposDataItemModel(
            id = 2, name = "Test", allow_forking = true, archive_url = "", archived = false, assignees_url = "", blobs_url = "", branches_url = "", clone_url = "", collaborators_url = "", comments_url = "", commits_url = "", compare_url = "", contents_url = "", contributors_url = "", created_at = "", default_branch = "main", deployments_url = "", description = null, disabled = false, downloads_url = "", events_url = "", fork = false, forks = 0, forks_count = 0, forks_url = "", full_name = "octocat/Test", git_commits_url = "", git_refs_url = "", git_tags_url = "", git_url = "", has_discussions = false, has_downloads = true, has_issues = true, has_pages = false, has_projects = true, has_wiki = true, homepage = null, hooks_url = "", html_url = "", is_template = false, issue_comment_url = "", issue_events_url = "", issues_url = "", keys_url = "", labels_url = "", language = "Kotlin", languages_url = "", license = null, merges_url = "", milestones_url = "", mirror_url = null, node_id = "", notifications_url = "", open_issues = 0, open_issues_count = 0, owner = sampleOwner, private = false, pulls_url = "", pushed_at = "", releases_url = "", size = 100, ssh_url = "", stargazers_count = 5, stargazers_url = "", statuses_url = "", subscribers_url = "", subscription_url = "", svn_url = "", tags_url = "", teams_url = "", topics = emptyList(), trees_url = "", updated_at = "", url = "", visibility = "public", watchers = 0, watchers_count = 0, web_commit_signoff_required = false
        ),
        ListReposDataItemModel(
            id = 3, name = "Test", allow_forking = true, archive_url = "", archived = false, assignees_url = "", blobs_url = "", branches_url = "", clone_url = "", collaborators_url = "", comments_url = "", commits_url = "", compare_url = "", contents_url = "", contributors_url = "", created_at = "", default_branch = "main", deployments_url = "", description = null, disabled = false, downloads_url = "", events_url = "", fork = false, forks = 0, forks_count = 0, forks_url = "", full_name = "octocat/Test", git_commits_url = "", git_refs_url = "", git_tags_url = "", git_url = "", has_discussions = false, has_downloads = true, has_issues = true, has_pages = false, has_projects = true, has_wiki = true, homepage = null, hooks_url = "", html_url = "", is_template = false, issue_comment_url = "", issue_events_url = "", issues_url = "", keys_url = "", labels_url = "", language = "Kotlin", languages_url = "", license = null, merges_url = "", milestones_url = "", mirror_url = null, node_id = "", notifications_url = "", open_issues = 0, open_issues_count = 0, owner = sampleOwner, private = false, pulls_url = "", pushed_at = "", releases_url = "", size = 100, ssh_url = "", stargazers_count = 5, stargazers_url = "", statuses_url = "", subscribers_url = "", subscription_url = "", svn_url = "", tags_url = "", teams_url = "", topics = emptyList(), trees_url = "", updated_at = "", url = "", visibility = "public", watchers = 0, watchers_count = 0, web_commit_signoff_required = false
        ),
        ListReposDataItemModel(
            id = 4, name = "Test", allow_forking = true, archive_url = "", archived = false, assignees_url = "", blobs_url = "", branches_url = "", clone_url = "", collaborators_url = "", comments_url = "", commits_url = "", compare_url = "", contents_url = "", contributors_url = "", created_at = "", default_branch = "main", deployments_url = "", description = null, disabled = false, downloads_url = "", events_url = "", fork = false, forks = 0, forks_count = 0, forks_url = "", full_name = "octocat/Test", git_commits_url = "", git_refs_url = "", git_tags_url = "", git_url = "", has_discussions = false, has_downloads = true, has_issues = true, has_pages = false, has_projects = true, has_wiki = true, homepage = null, hooks_url = "", html_url = "", is_template = false, issue_comment_url = "", issue_events_url = "", issues_url = "", keys_url = "", labels_url = "", language = "Kotlin", languages_url = "", license = null, merges_url = "", milestones_url = "", mirror_url = null, node_id = "", notifications_url = "", open_issues = 0, open_issues_count = 0, owner = sampleOwner, private = false, pulls_url = "", pushed_at = "", releases_url = "", size = 100, ssh_url = "", stargazers_count = 5, stargazers_url = "", statuses_url = "", subscribers_url = "", subscription_url = "", svn_url = "", tags_url = "", teams_url = "", topics = emptyList(), trees_url = "", updated_at = "", url = "", visibility = "public", watchers = 0, watchers_count = 0, web_commit_signoff_required = false
        )
    )
    GithubJetpackComposeTheme {
        RepoListScreen(
            state = GitState(repositories = mockRepos),
            onIntent = {}
        )
    }
}
