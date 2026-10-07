package com.example.githubjetpackcompose.data.model

import com.google.gson.annotations.SerializedName

data class UserDataJson(
    @SerializedName("avatar_url") val avatar_url: String? = null,
    @SerializedName("bio") val bio: Any? = null,
    @SerializedName("blog") val blog: String? = null,
    @SerializedName("company") val company: Any? = null,
    @SerializedName("created_at") val created_at: String? = null,
    @SerializedName("email") val email: Any? = null,
    @SerializedName("events_url") val events_url: String? = null,
    @SerializedName("followers") val followers: Int? = null,
    @SerializedName("followers_url") val followers_url: String? = null,
    @SerializedName("following") val following: Int? = null,
    @SerializedName("following_url") val following_url: String? = null,
    @SerializedName("gists_url") val gists_url: String? = null,
    @SerializedName("gravatar_id") val gravatar_id: String? = null,
    @SerializedName("hireable") val hireable: Any? = null,
    @SerializedName("html_url") val html_url: String? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("location") val location: Any? = null,
    @SerializedName("login") val login: String? = null,
    @SerializedName("name") val name: Any? = null,
    @SerializedName("node_id") val node_id: String? = null,
    @SerializedName("organizations_url") val organizations_url: String? = null,
    @SerializedName("public_gists") val public_gists: Int? = null,
    @SerializedName("public_repos") val public_repos: Int? = null,
    @SerializedName("received_events_url") val received_events_url: String? = null,
    @SerializedName("repos_url") val repos_url: String? = null,
    @SerializedName("site_admin") val site_admin: Boolean? = null,
    @SerializedName("starred_url") val starred_url: String? = null,
    @SerializedName("subscriptions_url") val subscriptions_url: String? = null,
    @SerializedName("twitter_username") val twitter_username: Any? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("updated_at") val updated_at: String? = null,
    @SerializedName("url") val url: String? = null
)
