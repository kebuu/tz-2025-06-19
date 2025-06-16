package com.example.rememberme.memory.infrastructure.persistence.model

import jakarta.persistence.CollectionTable
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "memory")
data class DbMemory(
    @Id
    val id: UUID,
    val text: String,
    val day: LocalDate,
    val userId: UUID,

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "memory_user_link", joinColumns = [JoinColumn(name = "memory_id")])
    val userLinks: List<DbMemoryUserLink>
)
