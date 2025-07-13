package com.example.rememberme.user.api.mcp

import com.example.rememberme.user.domain.usecase.GetUsersUseCase
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Component

@Component
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
class UserTool(private val getUsersUseCase: GetUsersUseCase) {

    companion object {
        const val LIST_USERS_TOOL_NAME = "list_users"
    }

    @Tool(name = LIST_USERS_TOOL_NAME, description = "Récupère la liste de tous les utilisateurs disponibles dans le système")
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
