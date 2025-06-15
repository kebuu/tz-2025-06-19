package com.example.rememberme.memory.domain.usecase

import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.memory.domain.spi.MemoryRepository
import com.example.rememberme.shared.domain.Id
import com.example.rememberme.shared.domain.usecase.UseCase
import com.example.rememberme.user.domain.User


class GetMemoryUseCase(private val memoryRepository: MemoryRepository) : UseCase<GetMemoryInput, Memory?> {
    override fun execute(input: GetMemoryInput): Memory? {
        return memoryRepository.findById(input.memoryId)
            ?.takeIf { memory -> memory.hasOwnerId(input.userId)}
    }
}

data class GetMemoryInput(
    val memoryId: Id<Memory>,
    val userId: Id<User>
)
