package com.example.rememberme.user.api.controller

import com.example.rememberme.shared.domain.Id
import com.example.rememberme.user.api.dto.CreateUserRequestDto
import com.example.rememberme.user.api.dto.UserDto
import com.example.rememberme.user.domain.Email
import com.example.rememberme.user.domain.Pseudo
import com.example.rememberme.user.domain.User
import com.example.rememberme.user.domain.usecase.CreateUserUseCase
import com.example.rememberme.user.domain.usecase.GetUserUseCase
import com.example.rememberme.user.domain.usecase.GetUsersUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.util.UUID
import kotlin.reflect.jvm.javaMethod

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = ["http://localhost:4200"])
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
class UserController(
    val userUseCase: GetUsersUseCase,
    val getUserUseCase: GetUserUseCase,
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

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: UUID): ResponseEntity<UserDto> {
        return getUserUseCase.findById(Id.Companion.of(id))
            ?.let { user ->
                ResponseEntity.ok(
                    UserDto(
                        id = user.id,
                        email = user.email,
                        pseudo = user.pseudo
                    )
                )
            }
            ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(
        @RequestBody request: CreateUserRequestDto
    ): ResponseEntity<UserDto> {
        val newUser = User(
            id = Id.Companion.random(),
            email = Email(request.email),
            pseudo = Pseudo(request.pseudo)
        )
        createUserUseCase.create(newUser)

        val userDto = UserDto(
            id = newUser.id,
            email = newUser.email,
            pseudo = newUser.pseudo
        )

        return ResponseEntity.created(
            MvcUriComponentsBuilder
                .fromMethod(
                    UserController::class.java,
                    UserController::getUser.javaMethod!!,
                    newUser.id.asString()
                )
                .build()
                .toUri()
        ).body(userDto)
    }
}
