package com.example.rememberme.memory.infrastructure.persistence.repository

import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.memory.domain.MemoryText
import com.example.rememberme.memory.domain.spi.MemoryRepository
import com.example.rememberme.memory.infrastructure.persistence.model.DbMemory
import com.example.rememberme.shared.domain.Id
import com.example.rememberme.user.domain.User
import org.springframework.stereotype.Component

@Component
class JpaMemoryRepository(private val jpaMemoryStore: JpaMemoryStore) : MemoryRepository {
    override fun findByUserId(userId: Id<User>): List<Memory> {
        return jpaMemoryStore.findAllByUserId(userId.value).map {
            toDomain(it)
        }
    }

    override fun findById(id: Id<Memory>): Memory? {
        return jpaMemoryStore.findMemoryById(id.value)?.let { dbMemory ->
            toDomain(dbMemory)
        }
    }

    override fun save(memory: Memory) {
        jpaMemoryStore.save(DbMemory(
            id = memory.id.value,
            text = memory.text.value,
            day = memory.day,
            userId = memory.userId.value
        ))
    }

    override fun delete(id: Id<Memory>) {
        jpaMemoryStore.findMemoryById(id.value)?.let {
            jpaMemoryStore.delete(it)
        }
    }

    private fun toDomain(memory: DbMemory): Memory = Memory(
        id = Id(memory.id),
        text = MemoryText(memory.text),
        day = memory.day,
        userId = Id(memory.userId)
    )
}
