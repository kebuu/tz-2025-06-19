package com.example.rememberme.user.infrastructure.persistence.repository

import com.example.rememberme.shared.domain.Id
import com.example.rememberme.user.domain.Email
import com.example.rememberme.user.domain.Pseudo
import com.example.rememberme.user.domain.User
import com.example.rememberme.user.domain.spi.UserRepository
import com.example.rememberme.user.infrastructure.persistence.model.DbUser
import org.springframework.stereotype.Component

@Component
class JpaUserRepository(private val jpaUserStore: JpaUserStore) : UserRepository {
    override fun findAll(): List<User> {
        return jpaUserStore.findAll().map {
            User(
                id = Id(it.id),
                email = Email(it.email),
                pseudo = Pseudo(it.pseudo)
            )
        }
    }

    override fun save(user: User) {
        jpaUserStore.save(DbUser(
            id = user.id.value,
            email = user.email.value,
            pseudo = user.pseudo.value
        ))
    }
}
