package com.example.rememberme.user.api

import com.example.rememberme.user.domain.User
import com.example.rememberme.user.domain.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

/**
 * Implementation of the UserService interface.
 */
@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    
    override fun findById(id: UUID): Optional<User> {
        return userRepository.findById(id)
    }
    
    override fun findByEmail(email: String): Optional<User> {
        return userRepository.findByEmail(email)
    }
    
    @Transactional
    override fun createUser(pseudo: String, email: String): User {
        val user = User(
            pseudo = pseudo,
            email = email
        )
        return userRepository.save(user)
    }
    
    @Transactional
    override fun updateUser(id: UUID, pseudo: String?, email: String?): Optional<User> {
        val optionalUser = userRepository.findById(id)
        
        if (optionalUser.isEmpty) {
            return Optional.empty()
        }
        
        val existingUser = optionalUser.get()
        val updatedUser = existingUser.copy(
            pseudo = pseudo ?: existingUser.pseudo,
            email = email ?: existingUser.email
        )
        
        return Optional.of(userRepository.save(updatedUser))
    }
    
    @Transactional
    override fun deleteUser(id: UUID): Boolean {
        val optionalUser = userRepository.findById(id)
        
        if (optionalUser.isEmpty) {
            return false
        }
        
        userRepository.delete(optionalUser.get())
        return true
    }
    
    override fun findAllUsers(): List<User> {
        return userRepository.findAll()
    }
}
