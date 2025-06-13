package com.example.rememberme.memory.api.dto

import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.shared.domain.Id
import java.time.LocalDate

data class MemoryDto(
    val id: Id<Memory>,
    val text: String,
    val day: LocalDate
)
