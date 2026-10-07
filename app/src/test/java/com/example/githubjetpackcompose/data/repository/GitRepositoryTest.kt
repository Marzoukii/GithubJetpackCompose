package com.example.githubjetpackcompose.data.repository

import com.example.githubjetpackcompose.data.NetworkResult
import com.example.githubjetpackcompose.data.model.DetailsDepotDataJson
import com.example.githubjetpackcompose.data.model.ListReposDataItemJson
import com.example.githubjetpackcompose.data.model.UserDataJson
import com.example.githubjetpackcompose.data.service.GitService
import com.example.githubjetpackcompose.domain.mapper.GitMapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class GitRepositoryTest {

    private val service = mockk<GitService>()
    private val repository = GitRepository(service, GitMapper())

    private fun <T> errorResponse(code: Int = 404): Response<T> =
        Response.error(code, ResponseBody.create(null, ""))

    // region getUser

    @Test
    fun `getUser emits Success with mapped user when response is successful`() = runTest {
        coEvery { service.getUser("Marzoukii") } returns
            Response.success(UserDataJson(login = "Marzoukii", id = 1))

        val results = repository.getUser("Marzoukii").toList()

        assertEquals(1, results.size)
        val user = (results.single() as NetworkResult.Success).data
        assertEquals("Marzoukii", user?.login)
        assertEquals(1, user?.id)
        coVerify(exactly = 1) { service.getUser("Marzoukii") }
    }

    @Test
    fun `getUser emits Error when response is not successful`() = runTest {
        coEvery { service.getUser(any()) } returns errorResponse()

        val result = repository.getUser("unknown").toList().single()

        assertTrue(result is NetworkResult.Error)
        assertTrue((result as NetworkResult.Error).exception.message!!.startsWith("Error:"))
    }

    @Test
    fun `getUser emits Error when service throws`() = runTest {
        val exception = IOException("no network")
        coEvery { service.getUser(any()) } throws exception

        val result = repository.getUser("Marzoukii").toList().single()

        assertEquals(exception, (result as NetworkResult.Error).exception)
    }

    // endregion

    // region getUserRepositories

    @Test
    fun `getUserRepositories emits Success with mapped list when response is successful`() = runTest {
        coEvery { service.getUserRepositories("Marzoukii") } returns Response.success(
            listOf(ListReposDataItemJson(name = "repo1"), ListReposDataItemJson(name = "repo2"))
        )

        val result = repository.getUserRepositories("Marzoukii").toList().single()

        val repos = (result as NetworkResult.Success).data
        assertEquals(listOf("repo1", "repo2"), repos?.map { it.name })
    }

    @Test
    fun `getUserRepositories emits Error when response is not successful`() = runTest {
        coEvery { service.getUserRepositories(any()) } returns errorResponse(500)

        val result = repository.getUserRepositories("Marzoukii").toList().single()

        assertTrue(result is NetworkResult.Error)
    }

    @Test
    fun `getUserRepositories emits Error when service throws`() = runTest {
        coEvery { service.getUserRepositories(any()) } throws IOException("timeout")

        val result = repository.getUserRepositories("Marzoukii").toList().single()

        assertEquals("timeout", (result as NetworkResult.Error).exception.message)
    }

    // endregion

    // region getRepositoryDetails

    @Test
    fun `getRepositoryDetails emits Success with mapped details when response is successful`() = runTest {
        coEvery { service.getRepositoryDetails("Marzoukii", "hello") } returns
            Response.success(DetailsDepotDataJson(name = "hello", network_count = 3))

        val result = repository.getRepositoryDetails("Marzoukii", "hello").toList().single()

        val details = (result as NetworkResult.Success).data
        assertEquals("hello", details?.name)
        assertEquals(3, details?.network_count)
        coVerify(exactly = 1) { service.getRepositoryDetails("Marzoukii", "hello") }
    }

    @Test
    fun `getRepositoryDetails emits Error when response is not successful`() = runTest {
        coEvery { service.getRepositoryDetails(any(), any()) } returns errorResponse()

        val result = repository.getRepositoryDetails("Marzoukii", "missing").toList().single()

        assertTrue(result is NetworkResult.Error)
    }

    @Test
    fun `getRepositoryDetails emits Error when service throws`() = runTest {
        coEvery { service.getRepositoryDetails(any(), any()) } throws IOException("no network")

        val result = repository.getRepositoryDetails("Marzoukii", "hello").toList().single()

        assertEquals("no network", (result as NetworkResult.Error).exception.message)
    }

    // endregion
}
