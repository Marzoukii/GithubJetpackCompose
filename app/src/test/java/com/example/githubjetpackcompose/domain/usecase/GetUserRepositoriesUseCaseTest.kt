package com.example.githubjetpackcompose.domain.usecase

import com.example.githubjetpackcompose.data.NetworkResult
import com.example.githubjetpackcompose.data.repository.GitRepository
import com.example.githubjetpackcompose.domain.model.ListReposDataItemModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetUserRepositoriesUseCaseTest {

    private val repository = mockk<GitRepository>()
    private val useCase = GetUserRepositoriesUseCase(repository)

    @Test
    fun `execute forwards Success from repository`() = runTest {
        val success = NetworkResult.Success(listOf(ListReposDataItemModel(name = "repo")))
        every { repository.getUserRepositories("Marzoukii") } returns flowOf(success)

        assertEquals(listOf(success), useCase.execute("Marzoukii").toList())
        verify(exactly = 1) { repository.getUserRepositories("Marzoukii") }
    }

    @Test
    fun `execute forwards Error from repository`() = runTest {
        val error = NetworkResult.Error(Exception("boom"))
        every { repository.getUserRepositories(any()) } returns flowOf(error)

        assertEquals(listOf(error), useCase.execute("Marzoukii").toList())
    }
}
