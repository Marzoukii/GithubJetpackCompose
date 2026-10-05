package com.example.githubjetpackcompose.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubjetpackcompose.R
import com.example.githubjetpackcompose.domain.model.DetailsDepotDataModel
import com.example.githubjetpackcompose.domain.model.OwnerModel
import com.example.githubjetpackcompose.ui.mvi.GitIntent
import com.example.githubjetpackcompose.ui.mvi.GitState
import com.example.githubjetpackcompose.ui.theme.GithubJetpackComposeTheme

private val BorderPurple = Color(0xFF6E7CF1)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoDetailScreen(
    state: GitState,
    onIntent: (GitIntent) -> Unit,
    username: String,
    repoName: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(username, repoName) {
        if (username.isNotBlank() && repoName.isNotBlank()) {
            onIntent(GitIntent.FetchRepositoryDetails(username, repoName))
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_login),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = repoName,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BorderPurple)
                }
            } else if (state.error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${state.error}",
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                val details = state.repositoryDetails
                if (details != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 10.dp,
                                    shape = RoundedCornerShape(20.dp),
                                    ambientColor = Color.Black.copy(alpha = 0.1f),
                                    spotColor = Color.Black.copy(alpha = 0.15f)
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.repos),
                                    contentDescription = "Repo Icon",
                                    modifier = Modifier.size(70.dp)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = details.name,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center
                                )

                                Text(
                                    text = details.full_name,
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )

                                details.description?.toString()?.let { desc ->
                                    if (desc.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = desc,
                                            fontSize = 14.sp,
                                            color = Color.DarkGray,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))

                                Spacer(modifier = Modifier.height(20.dp))

                                // Grid of Stats/Metrics with vector drawables
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    DetailStatItem(
                                        label = "Stars",
                                        value = "${details.stargazers_count}",
                                        iconRes = R.drawable.ic_star
                                    )
                                    DetailStatItem(
                                        label = "Forks",
                                        value = "${details.forks_count}",
                                        iconRes = R.drawable.ic_fork
                                    )
                                    DetailStatItem(
                                        label = "Watchers",
                                        value = "${details.watchers_count}",
                                        iconRes = R.drawable.ic_eye
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    DetailStatItem(
                                        label = "Issues",
                                        value = "${details.open_issues_count}",
                                        iconRes = R.drawable.ic_issue
                                    )
                                    DetailStatItem(
                                        label = "Language",
                                        value = details.language ?: "N/A",
                                        iconRes = R.drawable.ic_code
                                    )
                                    DetailStatItem(
                                        label = "Branch",
                                        value = details.default_branch,
                                        iconRes = R.drawable.ic_branch
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No details available.", color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailStatItem(
    label: String,
    value: String,
    iconRes: Int
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RepoDetailScreenPreview() {
    val sampleOwner = OwnerModel(
        avatar_url = "", events_url = "", followers_url = "", following_url = "",
        gists_url = "", gravatar_id = "", html_url = "", id = 1, login = "Marzoukii",
        node_id = "", organizations_url = "", received_events_url = "", repos_url = "",
        site_admin = false, starred_url = "", subscriptions_url = "", type = "User", url = ""
    )
    val mockDetails = DetailsDepotDataModel(
        allow_forking = true, archive_url = "", archived = false, assignees_url = "",
        blobs_url = "", branches_url = "", clone_url = "", collaborators_url = "",
        comments_url = "", commits_url = "", compare_url = "", contents_url = "",
        contributors_url = "", created_at = "", default_branch = "main", deployments_url = "",
        description = "This is a sample repository description", disabled = false, downloads_url = "",
        events_url = "", fork = false, forks = 12, forks_count = 12, forks_url = "",
        full_name = "Marzoukii/JetapckComposeProject", git_commits_url = "", git_refs_url = "", git_tags_url = "",
        git_url = "", has_discussions = false, has_downloads = true, has_issues = true,
        has_pages = false, has_projects = true, has_wiki = true, homepage = null, hooks_url = "",
        html_url = "", id = 1, is_template = false, issue_comment_url = "", issue_events_url = "",
        issues_url = "", keys_url = "", labels_url = "", language = "Kotlin", languages_url = "",
        license = null, merges_url = "", milestones_url = "", mirror_url = null, name = "JetapckComposeProject",
        network_count = 0, node_id = "", notifications_url = "", open_issues = 3, open_issues_count = 3,
        owner = sampleOwner, private = false, pulls_url = "", pushed_at = "", releases_url = "",
        size = 120, ssh_url = "", stargazers_count = 42, stargazers_url = "", statuses_url = "",
        subscribers_count = 5, subscribers_url = "", subscription_url = "", svn_url = "",
        tags_url = "", teams_url = "", temp_clone_token = null, topics = emptyList(), trees_url = "",
        updated_at = "", url = "", visibility = "public", watchers = 42, watchers_count = 42,
        web_commit_signoff_required = false
    )

    GithubJetpackComposeTheme {
        RepoDetailScreen(
            state = GitState(repositoryDetails = mockDetails),
            onIntent = {},
            username = "Marzoukii",
            repoName = "Jetpack-ComposeProject",
            onBack = {}
        )
    }
}
