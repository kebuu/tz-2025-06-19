package com.example.rememberme.user.api

import com.example.rememberme.user.domain.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * REST controller for User-related operations.
 */
@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {
    
    /**
     * Get a user by their ID.
     *
     * @param id The UUID of the user to find
     * @return The user if found, or 404 Not Found
     */
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: UUID): ResponseEntity<UserDto> {
        val optionalUser = userService.findById(id)
        
        return if (optionalUser.isPresent) {
            ResponseEntity.ok(UserDto.fromEntity(optionalUser.get()))
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    /**
     * Get a user by their email.
     *
     * @param email The email of the user to find
     * @return The user if found, or 404 Not Found
     */
    @GetMapping("/by-email")
    fun getUserByEmail(@RequestParam email: String): ResponseEntity<UserDto> {
        val optionalUser = userService.findByEmail(email)
        
        return if (optionalUser.isPresent) {
            ResponseEntity.ok(UserDto.fromEntity(optionalUser.get()))
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    /**
     * Create a new user.
     *
     * @param createUserDto The data for the new user
     * @return The created user
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody createUserDto: CreateUserDto): UserDto {
        val user = userService.createUser(
            pseudo = createUserDto.pseudo,
            email = createUserDto.email
        )
        
        return UserDto.fromEntity(user)
    }
    
    /**
     * Update an existing user.
     *
     * @param id The UUID of the user to update
     * @param updateUserDto The data to update
     * @return The updated user if found, or 404 Not Found
     */
    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: UUID,
        @RequestBody updateUserDto: UpdateUserDto
    ): ResponseEntity<UserDto> {
        val optionalUser = userService.updateUser(
            id = id,
            pseudo = updateUserDto.pseudo,
            email = updateUserDto.email
        )
        
        return if (optionalUser.isPresent) {
            ResponseEntity.ok(UserDto.fromEntity(optionalUser.get()))
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    /**
     * Delete a user by their ID.
     *
     * @param id The UUID of the user to delete
     * @return 204 No Content if deleted, or 404 Not Found
     */
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: UUID): ResponseEntity<Void> {
        val deleted = userService.deleteUser(id)
        
        return if (deleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    /**
     * Get all users.
     *
     * @return A list of all users
     */
    @GetMapping
    fun getAllUsers(): List<UserDto> {
        return userService.findAllUsers()
            .map { UserDto.fromEntity(it) }
    }
}
