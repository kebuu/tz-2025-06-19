package com.example.rememberme.memory.domain

import com.example.rememberme.shared.domain.Id
import com.example.rememberme.user.domain.User
import java.time.LocalDate

data class Memory(
    val id: Id<Memory>,
    val text: MemoryText,
    val day: LocalDate,
    val ownerId: Id<User>
) {
    fun hasOwnerId(ownerId: Id<User>): Boolean = this.ownerId == ownerId
}

@JvmInline
value class MemoryText(val value: String) {
    init {
        require(value.length <= 1000) { "Memory text cannot exceed 1000 characters" }
    }
}
