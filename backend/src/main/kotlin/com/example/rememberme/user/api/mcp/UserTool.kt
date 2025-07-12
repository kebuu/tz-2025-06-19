package com.example.rememberme.user.api.mcp

import com.example.rememberme.user.domain.usecase.GetUsersUseCase
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Component

@Component
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
class UserTool(private val getUsersUseCase: GetUsersUseCase) {

    /**
     * Récupère la liste de tous les utilisateurs
     */
    @Tool(name = "list_users", description = "Récupère la liste de tous les utilisateurs disponibles dans le système")
    fun listUsers(): List<UserDto> {
        return getUsersUseCase.execute()
            .map { user ->
                UserDto(
                    id = user.id.asString(),
                    name = user.pseudo.value,
                    email = user.email.value,
                )
            }
    }

    data class UserDto(
        val id: String,
        val name: String,
        val email: String,
    )
}
