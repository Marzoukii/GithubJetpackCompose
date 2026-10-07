package com.example.githubjetpackcompose.ui.mvi

import com.example.githubjetpackcompose.MainDispatcherRule
import com.example.githubjetpackcompose.data.NetworkResult
import com.example.githubjetpackcompose.domain.model.DetailsDepotDataModel
import com.example.githubjetpackcompose.domain.model.ListReposDataItemModel
import com.example.githubjetpackcompose.domain.model.UserDataModel
import com.example.githubjetpackcompose.domain.usecase.GetRepositoryDetailsUseCase
import com.example.githubjetpackcompose.domain.usecase.GetUserRepositoriesUseCase
import com.example.githubjetpackcompose.domain.usecase.GetUserUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GitViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getUserUseCase = mockk<GetUserUseCase>()
    private val getUserRepositoriesUseCase = mockk<GetUserRepositoriesUseCase>()
    private val getRepositoryDetailsUseCase = mockk<GetRepositoryDetailsUseCase>()

    private val viewModel by lazy {
        GitViewModel(getUserUseCase, getUserRepositoriesUseCase, getRepositoryDetailsUseCase)
    }

    @Test
    fun `initial state is empty`() {
        assertEquals(GitState(), viewModel.state.value)
    }

    // region FetchUser

    @Test
    fun `FetchUser sets loading while request is in progress`() = runTest {
        val results = Channel<NetworkResult<UserDataModel?>>()
        every { getUserUseCase.execute("Marzoukii") } returns results.receiveAsFlow()

        viewModel.handleIntent(GitIntent.FetchUser("Marzoukii"))
        assertTrue(viewModel.state.value.isLoading)

        results.send(NetworkResult.Success(UserDataModel(login = "Marzoukii")))
        advanceUntilIdle()
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `FetchUser success updates user`() = runTest {
        val user = UserDataModel(login = "Marzoukii")
        every { getUserUseCase.execute("Marzoukii") } returns flowOf(NetworkResult.Success(user))

        viewModel.handleIntent(GitIntent.FetchUser("Marzoukii"))

        with(viewModel.state.value) {
            assertFalse(isLoading)
            assertEquals(user, this.user)
            assertNull(error)
        }
        verify(exactly = 1) { getUserUseCase.execute("Marzoukii") }
    }

    @Test
    fun `FetchUser error sets error message`() = runTest {
        every { getUserUseCase.execute(any()) } returns flowOf(NetworkResult.Error(Exception("boom")))

        viewModel.handleIntent(GitIntent.FetchUser("Marzoukii"))

        with(viewModel.state.value) {
            assertFalse(isLoading)
            assertNull(user)
            assertEquals("boom", error)
        }
    }

    @Test
    fun `FetchUser clears previous error on new request`() = runTest {
        every { getUserUseCase.execute("bad") } returns flowOf(NetworkResult.Error(Exception("boom")))
        every { getUserUseCase.execute("Marzoukii") } returns
            flowOf(NetworkResult.Success(UserDataModel(login = "Marzoukii")))

        viewModel.handleIntent(GitIntent.FetchUser("bad"))
        assertEquals("boom", viewModel.state.value.error)

        viewModel.handleIntent(GitIntent.FetchUser("Marzoukii"))
        assertNull(viewModel.state.value.error)
        assertEquals("Marzoukii", viewModel.state.value.user?.login)
    }

    // endregion

    // region FetchUserRepositories

    @Test
    fun `FetchUserRepositories success updates repositories`() = runTest {
        val repos = listOf(ListReposDataItemModel(name = "repo1"), ListReposDataItemModel(name = "repo2"))
        every { getUserRepositoriesUseCase.execute("Marzoukii") } returns flowOf(NetworkResult.Success(repos))

        viewModel.handleIntent(GitIntent.FetchUserRepositories("Marzoukii"))

        with(viewModel.state.value) {
            assertFalse(isLoading)
            assertEquals(repos, repositories)
            assertNull(error)
        }
    }

    @Test
    fun `FetchUserRepositories error sets error message`() = runTest {
        every { getUserRepositoriesUseCase.execute(any()) } returns
            flowOf(NetworkResult.Error(Exception("server error")))

        viewModel.handleIntent(GitIntent.FetchUserRepositories("Marzoukii"))

        with(viewModel.state.value) {
            assertFalse(isLoading)
            assertNull(repositories)
            assertEquals("server error", error)
        }
    }

    // endregion

    // region FetchRepositoryDetails

    @Test
    fun `FetchRepositoryDetails success updates repositoryDetails`() = runTest {
        val details = DetailsDepotDataModel(name = "hello")
        every { getRepositoryDetailsUseCase.execute("Marzoukii", "hello") } returns
            flowOf(NetworkResult.Success(details))

        viewModel.handleIntent(GitIntent.FetchRepositoryDetails("Marzoukii", "hello"))

        with(viewModel.state.value) {
            assertFalse(isLoading)
            assertEquals(details, repositoryDetails)
            assertNull(error)
        }
        verify(exactly = 1) { getRepositoryDetailsUseCase.execute("Marzoukii", "hello") }
    }

    @Test
    fun `FetchRepositoryDetails error sets error message`() = runTest {
        every { getRepositoryDetailsUseCase.execute(any(), any()) } returns
            flowOf(NetworkResult.Error(Exception("not found")))

        viewModel.handleIntent(GitIntent.FetchRepositoryDetails("Marzoukii", "missing"))

        with(viewModel.state.value) {
            assertFalse(isLoading)
            assertNull(repositoryDetails)
            assertEquals("not found", error)
        }
    }

    // endregion
}
