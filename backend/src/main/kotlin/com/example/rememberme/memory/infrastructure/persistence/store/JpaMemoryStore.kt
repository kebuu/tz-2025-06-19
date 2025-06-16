package com.example.rememberme.memory.infrastructure.persistence.store

import com.example.rememberme.memory.infrastructure.persistence.model.DbMemory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface JpaMemoryStore : JpaRepository<DbMemory, UUID>, JpaSpecificationExecutor<DbMemory> {
    fun findMemoryById(id: UUID): DbMemory?

    fun findAllByUserId(userId: UUID): List<DbMemory>

    @Query("""select memory 
        from DbMemory memory 
        where memory.userLinks.userId = ?1 
        and memory.userLinks.userCanAccess = true"""
    )
    fun findByLinkedUserWithGrantedAccess(id: UUID): List<DbMemory>
}
