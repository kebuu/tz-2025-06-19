package com.example.rememberme.memory.api.dto

import java.time.LocalDate

data class UpdateMemoryRequestDto(
    val text: String,
    val day: LocalDate
)
