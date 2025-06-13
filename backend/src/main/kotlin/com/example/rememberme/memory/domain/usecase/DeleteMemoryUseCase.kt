package com.example.rememberme.memory.domain.usecase

import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.memory.domain.spi.MemoryRepository
import com.example.rememberme.shared.domain.Id
import com.example.rememberme.shared.domain.annotation.UseCase

@UseCase
class DeleteMemoryUseCase(
    private val memoryRepository: MemoryRepository
) {
    fun delete(id: Id<Memory>) {
        memoryRepository.delete(id)
    }
}
