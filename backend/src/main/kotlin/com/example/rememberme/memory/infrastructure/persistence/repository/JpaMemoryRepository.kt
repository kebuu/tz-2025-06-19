package com.example.rememberme.memory.infrastructure.persistence.repository

import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.memory.domain.MemoryText
import com.example.rememberme.memory.domain.MemoryUserLinkConfig
import com.example.rememberme.memory.domain.spi.MemoryRepository
import com.example.rememberme.memory.infrastructure.persistence.model.DbMemory
import com.example.rememberme.memory.infrastructure.persistence.model.DbMemoryUserLink
import com.example.rememberme.memory.infrastructure.persistence.store.JpaMemoryStore
import com.example.rememberme.shared.domain.Id
import com.example.rememberme.user.domain.User
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class JpaMemoryRepository(
    private val jpaMemoryStore: JpaMemoryStore
) : MemoryRepository {
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

    @Transactional
    override fun save(memory: Memory) {
        jpaMemoryStore.save(DbMemory(
            id = memory.id.value,
            text = memory.text.value,
            day = memory.day,
            userId = memory.ownerId.value,
            userLinks = memory.userLinks.map { DbMemoryUserLink(
                userId = it.userId.value,
                userCanAccess = it.userCanAccess
            )
            },
        ))
    }

    @Transactional
    override fun delete(id: Id<Memory>) {
        jpaMemoryStore.deleteById(id.value)
    }

    override fun findAllFilteredBy(filter: MemoryRepository.MemoryRepositoryFilter): List<Memory> {
        return when (filter) {
            is MemoryRepository.MemoryRepositoryFilter.LinkedUsedWithViewGrantedAccess -> {
                jpaMemoryStore.findByLinkedUserWithGrantedAccess(filter.userId.value)
            }
        }.map { dbMemory -> toDomain(dbMemory) }

    }

    private fun toDomain(memory: DbMemory): Memory = Memory(
        id = Id(memory.id),
        text = MemoryText(memory.text),
        day = memory.day,
        ownerId = Id(memory.userId),
        userLinks = memory.userLinks.map {
            MemoryUserLinkConfig(
                userId = Id(it.userId),
                userCanAccess = it.userCanAccess
            )
        }
    )
}
