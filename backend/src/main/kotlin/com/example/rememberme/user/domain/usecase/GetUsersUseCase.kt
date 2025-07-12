package com.example.rememberme.user.domain.usecase

import com.example.rememberme.shared.domain.usecase.ParameterLessUseCase
import com.example.rememberme.user.domain.User
import com.example.rememberme.user.domain.spi.UserRepository

class GetUsersUseCase(val userRepository: UserRepository) : ParameterLessUseCase<List<User>> {
    override fun execute(): List<User> {
        return userRepository.findAll()
    }
}
