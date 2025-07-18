package com.example.rememberme.user.domain

import com.example.rememberme.shared.domain.Id

data class User(
    val id: Id<User>,
    val email: Email,
    val pseudo: Pseudo,
    val preferences: UserPreferences = UserPreferences()
)

@JvmInline
value class Email(val value: String)

@JvmInline
value class Pseudo(val value: String)
