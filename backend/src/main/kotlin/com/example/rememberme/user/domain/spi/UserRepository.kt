package com.example.rememberme.user.domain.spi

import com.example.rememberme.shared.domain.Id
import com.example.rememberme.user.domain.User

interface UserRepository {

    fun findAll() : List<User>

    fun findById(id: Id<User>): User?

    fun save(user: User)
}
