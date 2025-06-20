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
        checkDayIsNotInFuture()
    }

    private fun checkDayIsNotInFuture() {
        check(day <= LocalDate.now()) { "Memory date cannot be in the future" }
    }

    fun hasOwnerId(ownerId: Id<User>): Boolean = this.ownerId == ownerId

    fun isAccessibleByUser(userId: Id<User>): Boolean {
        return hasOwnerId(userId) || userLinks.any { link -> link.userId == userId && link.userCanAccess }
    }

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

    companion object {
        const val MAX_VALUE_LENGTH = 1000
    }

    init {
        require(value.length <= MAX_VALUE_LENGTH) { "Memory text cannot exceed $MAX_VALUE_LENGTH characters" }
    }
}
