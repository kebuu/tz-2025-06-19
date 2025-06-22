package com.example.rememberme.user.infrastructure.persistence.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalTime
import java.util.UUID

@Entity
@Table(name = "user_preferences")
data class DbUserPreferences(
    @Id
    val userId: UUID,
    val missingDailyMemoryReminderTime: LocalTime?
)
