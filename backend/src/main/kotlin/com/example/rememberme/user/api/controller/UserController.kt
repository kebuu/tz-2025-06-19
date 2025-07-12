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
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.util.UUID
import kotlin.reflect.jvm.javaMethod

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = ["http://localhost:4200"])
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@Tag(name = "User", description = "User management API")
class UserController(
    val userUseCase: GetUsersUseCase,
    val getUserUseCase: GetUserUseCase,
    val createUserUseCase: CreateUserUseCase
) {

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves all users in the system")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved users",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = UserDto::class))])
    ])
    fun getAllUsers(): List<UserDto> = userUseCase.execute().map { user ->
        UserDto(
            id = user.id,
            email = user.email,
            pseudo = user.pseudo
        )
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by ID", description = "Retrieves a specific user by their ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved the user",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = UserDto::class))]),
        ApiResponse(responseCode = "404", description = "User not found", content = [Content()])
    ])
    fun getUser(
        @Parameter(description = "ID of the user to retrieve") @PathVariable id: UUID
    ): ResponseEntity<UserDto> {
        return getUserUseCase.execute(Id.Companion.of(id))
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
    @Operation(summary = "Create a new user", description = "Creates a new user in the system")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "User successfully created", content = [Content()]),
        ApiResponse(responseCode = "400", description = "Invalid input", content = [Content()])
    ])
    fun createUser(
        @Parameter(description = "User to create", required = true) 
        @RequestBody request: CreateUserRequestDto
    ): ResponseEntity<UserDto> {
        val newUser = User(
            id = Id.Companion.random(),
            email = Email(request.email),
            pseudo = Pseudo(request.pseudo)
        )
        createUserUseCase.execute(newUser)

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
