package com.example.rememberme.memory.api.dto

import com.example.rememberme.memory.domain.MemoryText
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class CreateMemoryRequestDto(
    @Size(max = MemoryText.MAX_VALUE_LENGTH)
    @NotBlank
    val text: String,

    @field:PastOrPresent
    @field:NotNull
    val day: LocalDate
)
