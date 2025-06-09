package com.example.rememberme.user.infrastructure.persistence.repository

import com.example.rememberme.shared.domain.Id
import com.example.rememberme.user.domain.Email
import com.example.rememberme.user.domain.Pseudo
import com.example.rememberme.user.domain.User
import com.example.rememberme.user.domain.spi.UserRepository
import org.springframework.stereotype.Component

@Component
class UserRepositoryAdapter(private val jpaUserRepository: JpaUserRepository) : UserRepository {
    override fun findAll(): List<User> {
        return jpaUserRepository.findAll().map {
            User(
                id = Id(it.id),
                email = Email(it.email),
                pseudo = Pseudo(it.pseudo)
            )
        }
    }
}
