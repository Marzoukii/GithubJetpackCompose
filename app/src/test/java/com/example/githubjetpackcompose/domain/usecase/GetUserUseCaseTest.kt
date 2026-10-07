package com.example.githubjetpackcompose.domain.usecase

import com.example.githubjetpackcompose.data.NetworkResult
import com.example.githubjetpackcompose.data.repository.GitRepository
import com.example.githubjetpackcompose.domain.model.UserDataModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetUserUseCaseTest {

    private val repository = mockk<GitRepository>()
    private val useCase = GetUserUseCase(repository)

    @Test
    fun `execute forwards Success from repository`() = runTest {
        val success = NetworkResult.Success(UserDataModel(login = "Marzoukii"))
        every { repository.getUser("Marzoukii") } returns flowOf(success)

        assertEquals(listOf(success), useCase.execute("Marzoukii").toList())
        verify(exactly = 1) { repository.getUser("Marzoukii") }
    }

    @Test
    fun `execute forwards Error from repository`() = runTest {
        val error = NetworkResult.Error(Exception("boom"))
        every { repository.getUser(any()) } returns flowOf(error)

        assertEquals(listOf(error), useCase.execute("Marzoukii").toList())
    }
}
