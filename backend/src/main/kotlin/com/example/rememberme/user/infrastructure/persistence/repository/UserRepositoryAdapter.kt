package com.example.rememberme.user.infrastructure.persistence.repository

import com.example.rememberme.user.domain.spi.UserRepository
import org.springframework.stereotype.Component

@Component
class UserRepositoryAdapter(private val jpaUserRepository: JpaUserRepository) : UserRepository {

}
