package com.example.rememberme.user.domain.usecase

import com.example.rememberme.shared.domain.usecase.UseCase
import com.example.rememberme.user.domain.User
import com.example.rememberme.user.domain.spi.UserRepository

class GetUsersUseCase(val userRepository: UserRepository) : UseCase<Unit, List<User>> {
    override fun execute(input: Unit): List<User> {
        return userRepository.findAll()
    }
}
