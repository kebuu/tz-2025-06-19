package com.example.rememberme.memory.domain

import com.example.rememberme.shared.domain.Id
import com.example.rememberme.user.domain.User
import java.time.LocalDate

data class Memory(
    val id: Id<Memory>,
    val text: MemoryText,
    val day: LocalDate,
    val ownerId: Id<User>,
    val userLinks: List<MemoryUserLinkConfig>
) {
    init {
        checkOwnerIsNotInUserLinks()
        checkNoUserLinkedMoreThanOnce()
    }

    fun hasOwnerId(ownerId: Id<User>): Boolean = this.ownerId == ownerId

    private fun checkOwnerIsNotInUserLinks() {
        check(userLinks.none { it.userId == ownerId }) { "Owner cannot be linked to its memory" }
    }

    private fun checkNoUserLinkedMoreThanOnce() {
        val firstDuplicateUserIdOrNull = userLinks
            .groupBy { it.userId }
            .filter { it.value.size > 1 }
            .keys.firstOrNull()
        check(firstDuplicateUserIdOrNull == null) { "User $firstDuplicateUserIdOrNull appears multiple times in memory links" }
    }
}

data class MemoryUserLinkConfig(
    val userId: Id<User>,
    val userCanAccess: Boolean
)

@JvmInline
value class MemoryText(val value: String) {
    init {
        require(value.length <= 1000) { "Memory text cannot exceed 1000 characters" }
    }
}
