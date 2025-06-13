package com.example.rememberme.memory.infrastructure.persistence.repository

import com.example.rememberme.memory.infrastructure.persistence.model.DbMemory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface JpaMemoryStore : JpaRepository<DbMemory, UUID> {
    fun findMemoryById(id: UUID): DbMemory?
    fun findAllByUserId(userId: UUID): List<DbMemory>
}
