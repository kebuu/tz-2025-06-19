package com.example.rememberme.user.api

import com.example.rememberme.user.domain.User
import java.util.Optional
import java.util.UUID

/**
 * Service interface for User-related operations.
 */
interface UserService {
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
     * Create a new user.
     *
     * @param pseudo The user's pseudo
     * @param email The user's email
     * @return The created user
     */
    fun createUser(pseudo: String, email: String): User
    
    /**
     * Update an existing user.
     *
     * @param id The UUID of the user to update
     * @param pseudo The new pseudo (or null to keep the existing value)
     * @param email The new email (or null to keep the existing value)
     * @return The updated user, or empty if the user was not found
     */
    fun updateUser(id: UUID, pseudo: String?, email: String?): Optional<User>
    
    /**
     * Delete a user by their ID.
     *
     * @param id The UUID of the user to delete
     * @return true if the user was deleted, false if the user was not found
     */
    fun deleteUser(id: UUID): Boolean
    
    /**
     * Find all users.
     *
     * @return A list of all users
     */
    fun findAllUsers(): List<User>
}
