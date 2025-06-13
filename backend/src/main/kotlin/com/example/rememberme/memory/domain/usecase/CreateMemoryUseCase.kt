package com.example.rememberme.memory.domain.usecase

import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.memory.domain.spi.MemoryRepository
import com.example.rememberme.shared.domain.usecase.UseCase

class CreateMemoryUseCase(
    private val memoryRepository: MemoryRepository
) : UseCase<Memory, Unit> {
    override fun execute(input: Memory) {
        memoryRepository.save(input)
    }
}
