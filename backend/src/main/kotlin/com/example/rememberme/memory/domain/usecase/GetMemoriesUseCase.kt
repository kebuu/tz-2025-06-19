package com.example.rememberme.memory.domain.usecase

import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.memory.domain.spi.MemoryRepository
import com.example.rememberme.shared.domain.Id
import com.example.rememberme.shared.domain.annotation.UseCase
import com.example.rememberme.user.domain.User

@UseCase
class GetMemoriesUseCase(val memoryRepository: MemoryRepository) {
    fun findByUserId(userId: Id<User>): List<Memory> {
        return memoryRepository.findByUserId(userId)
    }
}
