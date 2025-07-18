package com.example.rememberme.memory.domain.spi

import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.shared.domain.Id
import com.example.rememberme.user.domain.User
import java.time.LocalDate

interface MemoryRepository {

    fun findByUserId(userId: Id<User>): List<Memory>

    fun findById(id: Id<Memory>): Memory?

    fun save(memory: Memory)

    fun delete(id: Id<Memory>)

    fun existsByDayAndOwnerId(day: LocalDate, ownerId: Id<User>): Boolean

    fun findAllFilteredBy(filter: MemoryRepositoryFilter): List<Memory>

    sealed class MemoryRepositoryFilter {
        data class LinkedUsedWithViewGrantedAccess(val userId: Id<User>) : MemoryRepositoryFilter()
    }
}
