package com.example.rememberme.user.domain

import java.time.LocalTime

data class UserPreferences(
    val missingDailyMemoryReminderTime: LocalTime? = null
)
