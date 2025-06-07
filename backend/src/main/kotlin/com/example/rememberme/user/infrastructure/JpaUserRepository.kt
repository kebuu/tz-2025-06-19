package com.example.rememberme.user.infrastructure

import com.example.rememberme.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

/**
 * JPA implementation of the User repository.
 */
@Repository
interface JpaUserRepository : JpaRepository<User, UUID> {
    
    /**
     * Find a user by their email.
     *
     * @param email The email of the user to find
     * @return An Optional containing the user if found, or empty if not found
     */
    fun findByEmail(email: String): Optional<User>
}
