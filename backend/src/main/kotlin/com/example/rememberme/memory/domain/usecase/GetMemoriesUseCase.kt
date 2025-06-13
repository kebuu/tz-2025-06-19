package com.example.rememberme.memory.domain.usecase

import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.memory.domain.spi.MemoryRepository
import com.example.rememberme.shared.domain.annotation.UseCase

@UseCase
class GetMemoriesUseCase(val memoryRepository: MemoryRepository) {
    fun findAll(): List<Memory> {
        return memoryRepository.findAll()
    }
}
