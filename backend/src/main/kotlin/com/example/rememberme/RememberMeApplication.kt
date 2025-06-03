package com.example.rememberme

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Main application class for the RememberMe API.
 * This class serves as the entry point for the Spring Boot application.
 */
@SpringBootApplication
class RememberMeApplication

/**
 * Main function that starts the Spring Boot application.
 *
 * @param args Command line arguments
 */
fun main(args: Array<String>) {
    runApplication<RememberMeApplication>(*args)
}