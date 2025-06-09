package com.example.rememberme.user.domain.usecase

import com.example.rememberme.user.domain.spi.UserRepository

data class GetUsersUseCase(val userRepository: UserRepository)
