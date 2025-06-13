package com.example.rememberme.config

import com.example.rememberme.shared.domain.Id
import com.example.rememberme.user.domain.spi.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(private val userRepository: UserRepository) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/memories/**").authenticated()
                    .anyRequest().permitAll()
            }
            .httpBasic {}
            .build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username ->
            // Convert the username (which is a user ID) to a UUID
            val userId = try {
                java.util.UUID.fromString(username)
            } catch (e: IllegalArgumentException) {
                null
            }
            
            // Find the user by ID
            val user = userId?.let { id ->
                userRepository.findById(Id(id))
            }
            
            // If the user exists, create a UserDetails object with the fixed password
            if (user != null) {
                User.builder()
                    .username(user.id.value.toString())
                    .password("zenika")
                    .roles("USER")
                    .build()
            } else {
                throw org.springframework.security.core.userdetails.UsernameNotFoundException("User not found")
            }
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        // Using NoOpPasswordEncoder for simplicity since we have a fixed password
        // In a real application, you would use a secure password encoder like BCryptPasswordEncoder
        return NoOpPasswordEncoder.getInstance()
    }
}
