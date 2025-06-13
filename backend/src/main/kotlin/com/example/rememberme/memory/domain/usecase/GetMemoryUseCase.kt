package com.example.rememberme.memory.domain.usecase

import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.memory.domain.spi.MemoryRepository
import com.example.rememberme.shared.domain.Id
import com.example.rememberme.shared.domain.annotation.UseCase

@UseCase
class GetMemoryUseCase(private val memoryRepository: MemoryRepository) {
    fun findById(id: Id<Memory>): Memory? {
        return memoryRepository.findById(id)
    }
}
