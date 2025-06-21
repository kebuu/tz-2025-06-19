package com.example.rememberme.memory.domain.usecase

import arrow.core.Either
import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.memory.domain.MemoryDay
import com.example.rememberme.memory.domain.MemoryText
import com.example.rememberme.memory.domain.MemoryUserLinkConfig
import com.example.rememberme.memory.domain.spi.MemoryRepository
import com.example.rememberme.shared.domain.Id
import com.example.rememberme.user.domain.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.time.LocalDate
import java.util.UUID

class CreateMemoryUseCaseTest {

    private val memoryRepository = mock(MemoryRepository::class.java)
    private val createMemoryUseCase = CreateMemoryUseCase(memoryRepository)

    @Test
    fun `should save memory when it does not exist for the same date and owner`() {
        // Given
        val ownerId = Id<User>(UUID.randomUUID())
        val day = LocalDate.now()
        val memory = createValidMemory(ownerId, day)

        `when`(memoryRepository.existsByDayAndOwnerId(day, ownerId)).thenReturn(false)

        // When
        val result = createMemoryUseCase.execute(memory)

        // Then
        assertThat(result).isEqualTo(Either.Right(Unit))
        verify(memoryRepository).existsByDayAndOwnerId(day, ownerId)
        verify(memoryRepository).save(memory)
    }

    @Test
    fun `should return MemoryAlreadyExists when memory already exists for the same date and owner`() {
        // Given
        val ownerId = Id<User>(UUID.randomUUID())
        val day = LocalDate.now()
        val memory = createValidMemory(ownerId, day)

        `when`(memoryRepository.existsByDayAndOwnerId(day, ownerId)).thenReturn(true)

        // When
        val result = createMemoryUseCase.execute(memory)

        // Then
        assertThat(result).isEqualTo(Either.Left(MemoryAlreadyExists))
        verify(memoryRepository).existsByDayAndOwnerId(day, ownerId)
    }

    private fun createValidMemory(ownerId: Id<User>, day: LocalDate): Memory {
        return Memory(
            id = Id<Memory>(UUID.randomUUID()),
            text = MemoryText("This is a valid memory text"),
            day = MemoryDay(day),
            ownerId = ownerId,
            userLinks = listOf(
                MemoryUserLinkConfig(
                    userId = Id<User>(UUID.randomUUID()),
                    userCanAccess = true
                )
            )
        )
    }
}
