package com.example.rememberme.user.api

import com.example.rememberme.shared.domain.Id
import com.example.rememberme.user.domain.Email
import com.example.rememberme.user.domain.Pseudo
import com.example.rememberme.user.domain.User

data class UserDto(
    val id: Id<User>,
    val pseudo: Pseudo,
    val email: Email
)
