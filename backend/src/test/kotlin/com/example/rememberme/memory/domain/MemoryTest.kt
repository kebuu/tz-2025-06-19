package com.example.rememberme.memory.domain

import com.example.rememberme.shared.domain.Id
import com.example.rememberme.user.domain.User
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

class MemoryTest {

    @Test
    fun `should throw exception when owner is linked to its memory`() {
        // Given
        val ownerId = Id<User>(UUID.randomUUID())
        val text = MemoryText("This is a valid memory text")
        val day = LocalDate.now()
        val userLinks = listOf(
            MemoryUserLinkConfig(
                userId = ownerId, // Owner is linked to its memory
                userCanAccess = true
            )
        )

        // When/Then
        assertThatThrownBy {
            Memory(
                id = Id<Memory>(UUID.randomUUID()),
                text = text,
                day = day,
                ownerId = ownerId,
                userLinks = userLinks
            )
        }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("Owner cannot be linked to its memory")
    }

    @Test
    fun `should throw exception when a user is linked more than once`() {
        // Given
        val ownerId = Id<User>(UUID.randomUUID())
        val linkedUserId = Id<User>(UUID.randomUUID())
        val text = MemoryText("This is a valid memory text")
        val day = LocalDate.now()
        val userLinks = listOf(
            MemoryUserLinkConfig(
                userId = linkedUserId,
                userCanAccess = true
            ),
            MemoryUserLinkConfig(
                userId = linkedUserId, // Same user linked again
                userCanAccess = false
            )
        )

        // When/Then
        assertThatThrownBy {
            Memory(
                id = Id<Memory>(UUID.randomUUID()),
                text = text,
                day = day,
                ownerId = ownerId,
                userLinks = userLinks
            )
        }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("appears multiple times in memory links")
    }

    @Test
    fun `should throw exception when memory text exceeds 1000 characters`() {
        // Given
        val ownerId = Id<User>(UUID.randomUUID())
        val day = LocalDate.now()
        val userLinks = listOf(
            MemoryUserLinkConfig(
                userId = Id<User>(UUID.randomUUID()),
                userCanAccess = true
            )
        )
        val longText = "a".repeat(MemoryText.MAX_VALUE_LENGTH + 1) // Text exceeding max length

        // When/Then
        assertThatThrownBy {
            Memory(
                id = Id<Memory>(UUID.randomUUID()),
                text = MemoryText(longText),
                day = day,
                ownerId = ownerId,
                userLinks = userLinks
            )
        }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Memory text cannot exceed ${MemoryText.MAX_VALUE_LENGTH} characters")
    }

    @Test
    fun `should check if memory has specific owner id`() {
        // Given
        val ownerId = Id<User>(UUID.randomUUID())
        val memory = Memory(
            id = Id<Memory>(UUID.randomUUID()),
            text = MemoryText("This is a valid memory text"),
            day = LocalDate.now(),
            ownerId = ownerId,
            userLinks = listOf(
                MemoryUserLinkConfig(
                    userId = Id<User>(UUID.randomUUID()),
                    userCanAccess = true
                )
            )
        )
        val anotherOwnerId = Id<User>(UUID.randomUUID())

        // When/Then
        assertThat(memory.hasOwnerId(ownerId)).isTrue()
        assertThat(memory.hasOwnerId(anotherOwnerId)).isFalse()
    }
}
