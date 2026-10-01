package com.example.githubjetpackcompose.data.model

import com.google.gson.annotations.SerializedName

data class UserDataJson(
    @SerializedName("avatar_url") val avatar_url: String,
    @SerializedName("bio") val bio: Any?,
    @SerializedName("blog") val blog: String,
    @SerializedName("company") val company: Any?,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("email") val email: Any?,
    @SerializedName("events_url") val events_url: String,
    @SerializedName("followers") val followers: Int,
    @SerializedName("followers_url") val followers_url: String,
    @SerializedName("following") val following: Int,
    @SerializedName("following_url") val following_url: String,
    @SerializedName("gists_url") val gists_url: String,
    @SerializedName("gravatar_id") val gravatar_id: String,
    @SerializedName("hireable") val hireable: Any?,
    @SerializedName("html_url") val html_url: String,
    @SerializedName("id") val id: Int,
    @SerializedName("location") val location: Any?,
    @SerializedName("login") val login: String,
    @SerializedName("name") val name: Any?,
    @SerializedName("node_id") val node_id: String,
    @SerializedName("organizations_url") val organizations_url: String,
    @SerializedName("public_gists") val public_gists: Int,
    @SerializedName("public_repos") val public_repos: Int,
    @SerializedName("received_events_url") val received_events_url: String,
    @SerializedName("repos_url") val repos_url: String,
    @SerializedName("site_admin") val site_admin: Boolean,
    @SerializedName("starred_url") val starred_url: String,
    @SerializedName("subscriptions_url") val subscriptions_url: String,
    @SerializedName("twitter_username") val twitter_username: Any?,
    @SerializedName("type") val type: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("url") val url: String
)