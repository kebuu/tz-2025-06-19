package com.example.rememberme.memory.api.mcp

import com.example.rememberme.memory.api.dto.MemoryDto
import com.example.rememberme.memory.domain.usecase.GetMemoriesUseCase
import com.example.rememberme.shared.domain.Id
import com.example.rememberme.user.domain.User
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Component
import java.util.UUID

@Component
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
class MemoryTool(private val getMemoriesUseCase: GetMemoriesUseCase) {

    /**
     * Récupère la liste des souvenirs d'un utilisateur
     */
    @Tool(name = "list_user_memories", description = "Récupère la liste des souvenirs d'un utilisateur spécifique")
    fun listUserMemories(userId: String): List<MemoryDto> {
        val userIdObj = Id.of<User>(UUID.fromString(userId))
        return getMemoriesUseCase.execute(userIdObj)
            .map { memory ->
                MemoryDto(
                    id = memory.id,
                    text = memory.text.value,
                    day = memory.day.value
                )
            }
    }
}
