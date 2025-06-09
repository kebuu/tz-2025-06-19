package com.example.rememberme.user.api

import com.example.rememberme.user.domain.User
import com.example.rememberme.user.domain.usecase.GetUsersUseCase
import jakarta.annotation.PostConstruct
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/users")
class UserController(val userUseCase: GetUsersUseCase) {

    @GetMapping
    fun getAllUsers(): List<User> = userUseCase.findAll()


    @PostConstruct
    fun init() {
        println("UserController init")
    }
}
