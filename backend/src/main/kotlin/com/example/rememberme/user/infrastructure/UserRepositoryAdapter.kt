package com.example.rememberme.user.infrastructure

import com.example.rememberme.user.domain.User
import com.example.rememberme.user.domain.UserRepository
import org.springframework.stereotype.Component
import java.util.Optional
import java.util.UUID

/**
 * Adapter implementation of UserRepository that delegates to JpaUserRepository.
 * This follows the Adapter pattern in DDD, bridging the domain and infrastructure layers.
 */
@Component
class UserRepositoryAdapter(private val jpaUserRepository: JpaUserRepository) : UserRepository {
    
    override fun findById(id: UUID): Optional<User> {
        return jpaUserRepository.findById(id)
    }
    
    override fun findByEmail(email: String): Optional<User> {
        return jpaUserRepository.findByEmail(email)
    }
    
    override fun save(user: User): User {
        return jpaUserRepository.save(user)
    }
    
    override fun delete(user: User) {
        jpaUserRepository.delete(user)
    }
    
    override fun findAll(): List<User> {
        return jpaUserRepository.findAll()
    }
}
