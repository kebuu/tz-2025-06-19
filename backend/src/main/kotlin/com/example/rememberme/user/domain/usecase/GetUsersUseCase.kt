package com.example.rememberme.user.domain.usecase

import com.example.rememberme.shared.domain.annotation.UseCase
import jakarta.annotation.PostConstruct

@UseCase
class GetUsersUseCase() {

    @PostConstruct
    fun init() {
        println("Users use case initialized")
    }
}
