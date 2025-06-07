package com.example.rememberme.user.domain

import java.util.UUID
import java.util.Optional

/**
 * Repository interface for User entity operations.
 */
interface UserRepository {
    /**
     * Find a user by their ID.
     *
     * @param id The UUID of the user to find
     * @return An Optional containing the user if found, or empty if not found
     */
    fun findById(id: UUID): Optional<User>
    
    /**
     * Find a user by their email.
     *
     * @param email The email of the user to find
     * @return An Optional containing the user if found, or empty if not found
     */
    fun findByEmail(email: String): Optional<User>
    
    /**
     * Save a user to the repository.
     *
     * @param user The user to save
     * @return The saved user
     */
    fun save(user: User): User
    
    /**
     * Delete a user from the repository.
     *
     * @param user The user to delete
     */
    fun delete(user: User)
    
    /**
     * Find all users in the repository.
     *
     * @return A list of all users
     */
    fun findAll(): List<User>
}
