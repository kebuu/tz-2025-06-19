package com.example.rememberme.user.domain.usecase

import com.example.rememberme.shared.domain.Id
import com.example.rememberme.shared.domain.usecase.UseCase
import com.example.rememberme.user.domain.User
import com.example.rememberme.user.domain.spi.UserRepository

class GetUserUseCase(private val userRepository: UserRepository) : UseCase<Id<User>, User?> {
    override fun execute(input: Id<User>): User? {
        return userRepository.findById(input)
    }
}
