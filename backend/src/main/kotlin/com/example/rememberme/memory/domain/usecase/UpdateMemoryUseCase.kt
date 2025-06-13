package com.example.rememberme.memory.domain.usecase

import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.memory.domain.spi.MemoryRepository
import com.example.rememberme.shared.domain.annotation.UseCase

@UseCase
class UpdateMemoryUseCase(
    private val memoryRepository: MemoryRepository
) {
    fun update(memory: Memory) {
        memoryRepository.save(memory)
    }
}
