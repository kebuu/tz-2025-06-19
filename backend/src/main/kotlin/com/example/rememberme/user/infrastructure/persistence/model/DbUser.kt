package com.example.rememberme.user.infrastructure.persistence.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.PrimaryKeyJoinColumn
import jakarta.persistence.Table
import java.util.UUID

/**
 * User entity representing a user in the system.
 */
@Entity
@Table(name = "app_user")
data class DbUser(
    @Id
    val id: UUID,
    val pseudo: String,
    val email: String,

    @OneToOne
    @PrimaryKeyJoinColumn
    val preferences: DbUserPreferences? = null
)
