package com.example.rememberme.user.api

import com.example.rememberme.user.domain.usecase.GetUsersUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/users")
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
class UserController(
    val userUseCase: GetUsersUseCase
) {

    @GetMapping
    fun getAllUsers(): List<UserDto> = userUseCase.findAll().map { user ->
        UserDto(
            id = user.id,
            email = user.email,
            pseudo = user.pseudo
        )
    }
}
