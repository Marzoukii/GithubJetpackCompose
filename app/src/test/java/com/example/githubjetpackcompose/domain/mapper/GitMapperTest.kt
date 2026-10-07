package com.example.githubjetpackcompose.domain.mapper

import com.example.githubjetpackcompose.data.model.DetailsDepotDataJson
import com.example.githubjetpackcompose.data.model.ListReposDataItemJson
import com.example.githubjetpackcompose.data.model.OwnerJson
import com.example.githubjetpackcompose.data.model.UserDataJson
import com.example.githubjetpackcompose.domain.model.OwnerModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GitMapperTest {

    private val mapper = GitMapper()

    // region toUserDataModel

    @Test
    fun `toUserDataModel maps all provided fields`() {
        val json = UserDataJson(
            login = "Marzoukii",
            id = 42,
            name = "Marzoukii",
            followers = 10,
            following = 5,
            public_repos = 8,
            public_gists = 2,
            site_admin = true,
            avatar_url = "https://avatar",
            bio = "bio",
            location = "Tunisie"
        )

        val model = mapper.toUserDataModel(json)

        assertEquals("Marzoukii", model.login)
        assertEquals(42, model.id)
        assertEquals("Marzoukii", model.name)
        assertEquals(10, model.followers)
        assertEquals(5, model.following)
        assertEquals(8, model.public_repos)
        assertEquals(2, model.public_gists)
        assertTrue(model.site_admin)
        assertEquals("https://avatar", model.avatar_url)
        assertEquals("bio", model.bio)
        assertEquals("Tunisie", model.location)
    }

    @Test
    fun `toUserDataModel with null json returns defaults`() {
        val model = mapper.toUserDataModel(null)

        assertNull(model.login)
        assertEquals(0, model.id)
        assertEquals(0, model.followers)
        assertEquals(0, model.following)
        assertEquals(0, model.public_repos)
        assertEquals(0, model.public_gists)
        assertFalse(model.site_admin)
    }

    @Test
    fun `toUserDataModel with null numeric fields falls back to defaults`() {
        val model = mapper.toUserDataModel(UserDataJson(login = "Marzoukii"))

        assertEquals("Marzoukii", model.login)
        assertEquals(0, model.id)
        assertEquals(0, model.followers)
        assertFalse(model.site_admin)
    }

    // endregion

    // region toOwnerModel

    @Test
    fun `toOwnerModel maps all provided fields`() {
        val model = mapper.toOwnerModel(
            OwnerJson(login = "Marzoukii", id = 1, avatar_url = "https://avatar", site_admin = true)
        )

        assertEquals("Marzoukii", model.login)
        assertEquals(1, model.id)
        assertEquals("https://avatar", model.avatar_url)
        assertTrue(model.site_admin)
    }

    @Test
    fun `toOwnerModel with null json returns default OwnerModel`() {
        assertEquals(OwnerModel(), mapper.toOwnerModel(null))
    }

    // endregion

    // region toListReposDataItemModel

    @Test
    fun `toListReposDataItemModel maps repo and nested owner`() {
        val json = ListReposDataItemJson(
            id = 7,
            name = "hello-world",
            full_name = "Marzoukii/hello-world",
            description = "My first repo",
            language = "Kotlin",
            stargazers_count = 100,
            forks_count = 20,
            fork = true,
            topics = listOf("android", "compose"),
            owner = OwnerJson(login = "Marzoukii", id = 1)
        )

        val model = mapper.toListReposDataItemModel(json)

        assertEquals(7, model.id)
        assertEquals("hello-world", model.name)
        assertEquals("Marzoukii/hello-world", model.full_name)
        assertEquals("My first repo", model.description)
        assertEquals("Kotlin", model.language)
        assertEquals(100, model.stargazers_count)
        assertEquals(20, model.forks_count)
        assertTrue(model.fork)
        assertEquals(listOf("android", "compose"), model.topics)
        assertEquals("Marzoukii", model.owner.login)
        assertEquals(1, model.owner.id)
    }

    @Test
    fun `toListReposDataItemModel with null json returns defaults`() {
        val model = mapper.toListReposDataItemModel(null as ListReposDataItemJson?)

        assertNull(model.name)
        assertEquals(0, model.id)
        assertEquals(0, model.stargazers_count)
        assertFalse(model.fork)
        assertTrue(model.topics.isEmpty())
        assertEquals(OwnerModel(), model.owner)
    }

    @Test
    fun `toListReposDataItemModel list maps every item in order`() {
        val result = mapper.toListReposDataItemModel(
            listOf(ListReposDataItemJson(id = 1, name = "a"), ListReposDataItemJson(id = 2, name = "b"))
        )

        assertEquals(listOf("a", "b"), result?.map { it.name })
        assertEquals(listOf(1, 2), result?.map { it.id })
    }

    @Test
    fun `toListReposDataItemModel with null list returns null`() {
        assertNull(mapper.toListReposDataItemModel(null as List<ListReposDataItemJson>?))
    }

    @Test
    fun `toListReposDataItemModel with empty list returns empty list`() {
        assertEquals(emptyList<Any>(), mapper.toListReposDataItemModel(emptyList<ListReposDataItemJson>()))
    }

    // endregion

    // region toDetailsDepotDataModel

    @Test
    fun `toDetailsDepotDataModel maps repo details and nested owner`() {
        val json = DetailsDepotDataJson(
            id = 7,
            name = "hello-world",
            full_name = "Marzoukii/hello-world",
            language = "Kotlin",
            network_count = 3,
            subscribers_count = 4,
            watchers_count = 5,
            open_issues_count = 6,
            archived = true,
            owner = OwnerJson(login = "Marzoukii")
        )

        val model = mapper.toDetailsDepotDataModel(json)

        assertEquals(7, model.id)
        assertEquals("hello-world", model.name)
        assertEquals("Marzoukii/hello-world", model.full_name)
        assertEquals("Kotlin", model.language)
        assertEquals(3, model.network_count)
        assertEquals(4, model.subscribers_count)
        assertEquals(5, model.watchers_count)
        assertEquals(6, model.open_issues_count)
        assertTrue(model.archived)
        assertEquals("Marzoukii", model.owner.login)
    }

    @Test
    fun `toDetailsDepotDataModel with null json returns defaults`() {
        val model = mapper.toDetailsDepotDataModel(null)

        assertNull(model.name)
        assertEquals(0, model.id)
        assertEquals(0, model.network_count)
        assertEquals(0, model.subscribers_count)
        assertFalse(model.archived)
        assertTrue(model.topics.isEmpty())
        assertEquals(OwnerModel(), model.owner)
    }

    // endregion
}
