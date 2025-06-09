package com.example.rememberme.shared.domain

import java.util.UUID

@JvmInline value class Id<out T>(val value: UUID)
