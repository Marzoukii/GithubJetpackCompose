package com.example.githubjetpackcompose.domain.usecase

import com.example.githubjetpackcompose.data.NetworkResult
import com.example.githubjetpackcompose.data.repository.GitRepository
import com.example.githubjetpackcompose.domain.model.DetailsDepotDataModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetRepositoryDetailsUseCaseTest {

    private val repository = mockk<GitRepository>()
    private val useCase = GetRepositoryDetailsUseCase(repository)

    @Test
    fun `execute forwards Success from repository`() = runTest {
        val success = NetworkResult.Success(DetailsDepotDataModel(name = "hello"))
        every { repository.getRepositoryDetails("Marzoukii", "hello") } returns flowOf(success)

        assertEquals(listOf(success), useCase.execute("Marzoukii", "hello").toList())
        verify(exactly = 1) { repository.getRepositoryDetails("Marzoukii", "hello") }
    }

    @Test
    fun `execute forwards Error from repository`() = runTest {
        val error = NetworkResult.Error(Exception("boom"))
        every { repository.getRepositoryDetails(any(), any()) } returns flowOf(error)

        assertEquals(listOf(error), useCase.execute("Marzoukii", "hello").toList())
    }
}
