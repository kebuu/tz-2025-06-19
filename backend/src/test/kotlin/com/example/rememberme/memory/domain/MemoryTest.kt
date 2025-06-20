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

    @Test
    fun `should throw exception when memory date is in the future`() {
        // Given
        val ownerId = Id<User>(UUID.randomUUID())
        val text = MemoryText("This is a valid memory text")
        val futureDay = LocalDate.now().plusDays(1) // Date in the future
        val userLinks = emptyList<MemoryUserLinkConfig>()

        // When/Then
        assertThatThrownBy {
            Memory(
                id = Id<Memory>(UUID.randomUUID()),
                text = text,
                day = futureDay,
                ownerId = ownerId,
                userLinks = userLinks
            )
        }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("Memory date cannot be in the future")
    }

    @Test
    fun `should check if memory is accessible by user`() {
        // Given
        val ownerId = Id<User>(UUID.randomUUID())
        val linkedUserWithAccessId = Id<User>(UUID.randomUUID())
        val linkedUserWithoutAccessId = Id<User>(UUID.randomUUID())
        val notLinkedUserId = Id<User>(UUID.randomUUID())

        val memory = Memory(
            id = Id<Memory>(UUID.randomUUID()),
            text = MemoryText("This is a valid memory text"),
            day = LocalDate.now(),
            ownerId = ownerId,
            userLinks = listOf(
                MemoryUserLinkConfig(
                    userId = linkedUserWithAccessId,
                    userCanAccess = true
                ),
                MemoryUserLinkConfig(
                    userId = linkedUserWithoutAccessId,
                    userCanAccess = false
                )
            )
        )

        // When/Then
        // Owner should have access
        assertThat(memory.isAccessibleByUser(ownerId)).isTrue()

        // Linked user with access should have access
        assertThat(memory.isAccessibleByUser(linkedUserWithAccessId)).isTrue()

        // Linked user without access should not have access
        assertThat(memory.isAccessibleByUser(linkedUserWithoutAccessId)).isFalse()

        // Not linked user should not have access
        assertThat(memory.isAccessibleByUser(notLinkedUserId)).isFalse()
    }
}
