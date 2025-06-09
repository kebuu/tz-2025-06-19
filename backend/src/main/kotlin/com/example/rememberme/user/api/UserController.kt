package com.example.rememberme.user.api

import com.example.rememberme.shared.domain.Id
import com.example.rememberme.user.domain.Email
import com.example.rememberme.user.domain.Pseudo
import com.example.rememberme.user.domain.User
import com.example.rememberme.user.domain.usecase.CreateUserUseCase
import com.example.rememberme.user.domain.usecase.GetUsersUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.net.URI


@RestController
@RequestMapping("/users")
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
class UserController(
    val userUseCase: GetUsersUseCase,
    val createUserUseCase: CreateUserUseCase
) {

    @GetMapping
    fun getAllUsers(): List<UserDto> = userUseCase.findAll().map { user ->
        UserDto(
            id = user.id,
            email = user.email,
            pseudo = user.pseudo
        )
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(
        @RequestBody request: CreateUserRequestDto
    ): ResponseEntity<Void> {
        createUserUseCase.create(User(
            id = Id.random(),
            email = Email(request.email),
            pseudo = Pseudo(request.pseudo)
        ))

        return ResponseEntity.created(URI.create("http://localhost:8080")).build()
    }
}
