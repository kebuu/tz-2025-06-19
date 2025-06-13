package com.example.rememberme.memory.api.dto

import java.time.LocalDate

data class CreateMemoryRequestDto(
    val text: String,
    val day: LocalDate
)
