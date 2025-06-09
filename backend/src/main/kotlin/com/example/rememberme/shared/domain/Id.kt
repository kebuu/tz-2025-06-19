package com.example.rememberme.shared.domain

import java.util.UUID

/**
 * A generic identifier wrapping a UUID.
 * @param T The type of the entity this identifier is associated with. It is not used but necessary for type safety.
 */
@Suppress("unused")
@JvmInline value class Id<out T>(val value: UUID) {

    fun asString(): String = value.toString()

    companion object {
        inline fun <reified T> of(value: UUID): Id<T> = Id(value)
        inline fun <reified T> random(): Id<T> = Id(UUID.randomUUID())
    }
}
