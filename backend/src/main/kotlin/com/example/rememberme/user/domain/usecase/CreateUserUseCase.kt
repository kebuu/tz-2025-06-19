package com.example.rememberme.user.domain.usecase

import com.example.rememberme.shared.domain.usecase.UseCase
import com.example.rememberme.user.domain.User
import com.example.rememberme.user.domain.spi.UserRepository

class CreateUserUseCase(
    private val userRepository: UserRepository
) : UseCase<User, Unit> {
    override fun execute(input: User) {
        userRepository.save(input)
    }
}
