package com.example.rememberme.user.infrastructure.persistence.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

/**
 * User entity representing a user in the system.
 */
@Entity
@Table(name = "user")
data class User(
    @Id
    val id: UUID,
    
    val pseudo: String,
    
    val email: String
)
