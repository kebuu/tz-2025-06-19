package com.example.rememberme.memory.infrastructure.persistence.model

import jakarta.persistence.Embeddable
import java.util.UUID

@Embeddable
data class DbMemoryUserLink(
    val userId: UUID,
    val userCanAccess: Boolean
)
