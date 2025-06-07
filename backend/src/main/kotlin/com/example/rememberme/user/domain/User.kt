package com.example.rememberme.user.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

/**
 * User entity representing a user in the system.
 */
@Entity
@Table(name = "users")
data class User(
    @Id
    val id: UUID = UUID.randomUUID(),
    
    val pseudo: String,
    
    val email: String
)
