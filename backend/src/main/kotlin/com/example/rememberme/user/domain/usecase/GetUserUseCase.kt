package com.example.rememberme.user.domain.usecase

import com.example.rememberme.shared.domain.Id
import com.example.rememberme.shared.domain.annotation.UseCase
import com.example.rememberme.user.domain.User
import com.example.rememberme.user.domain.spi.UserRepository

@UseCase
class GetUserUseCase(private val userRepository: UserRepository) {
    fun findById(id: Id<User>): User? {
        return userRepository.findById(id)
    }
}
