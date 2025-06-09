package com.example.rememberme.user.domain.spi

import com.example.rememberme.user.domain.User

interface UserRepository {

    fun findAll() : List<User>

    fun save(user: User)
}
