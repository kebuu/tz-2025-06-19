package com.example.rememberme.user.domain.usecase

import com.example.rememberme.shared.domain.annotation.UseCase
import com.example.rememberme.user.domain.User
import com.example.rememberme.user.domain.spi.UserRepository

@UseCase
class CreateUserUseCase(
    private val userRepository: UserRepository
) {
    fun create(newUser: User) {
        userRepository.save(newUser)
    }
}
