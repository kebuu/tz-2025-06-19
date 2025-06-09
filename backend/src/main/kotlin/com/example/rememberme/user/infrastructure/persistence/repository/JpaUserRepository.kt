package com.example.rememberme.user.infrastructure.persistence.repository

import com.example.rememberme.user.infrastructure.persistence.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface JpaUserRepository : JpaRepository<User, UUID> {

    fun findByEmail(email: String): Optional<User>
}
