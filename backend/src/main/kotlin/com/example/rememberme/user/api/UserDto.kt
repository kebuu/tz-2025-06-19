package com.example.rememberme.user.api

import com.example.rememberme.user.domain.User
import java.util.UUID

/**
 * Data Transfer Object for User entity.
 */
data class UserDto(
    val id: UUID,
    val pseudo: String,
    val email: String
) {
    companion object {
        /**
         * Convert a User entity to a UserDto.
         *
         * @param user The User entity to convert
         * @return The corresponding UserDto
         */
        fun fromEntity(user: User): UserDto {
            return UserDto(
                id = user.id,
                pseudo = user.pseudo,
                email = user.email
            )
        }
    }
}

/**
 * Data Transfer Object for creating a new User.
 */
data class CreateUserDto(
    val pseudo: String,
    val email: String
)

/**
 * Data Transfer Object for updating an existing User.
 */
data class UpdateUserDto(
    val pseudo: String?,
    val email: String?
)
