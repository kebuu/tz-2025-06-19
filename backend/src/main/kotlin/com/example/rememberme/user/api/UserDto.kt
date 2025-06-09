package com.example.rememberme.user.api

import java.util.UUID

data class UserDto(
    val id: UUID,
    val pseudo: String,
    val email: String
)
