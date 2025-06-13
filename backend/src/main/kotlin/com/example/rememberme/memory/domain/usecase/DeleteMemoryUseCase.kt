package com.example.rememberme.memory.domain.usecase

import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.memory.domain.spi.MemoryRepository
import com.example.rememberme.shared.domain.Id
import com.example.rememberme.shared.domain.usecase.UseCase

class DeleteMemoryUseCase(
    private val memoryRepository: MemoryRepository
) : UseCase<Id<Memory>, Unit> {
    override fun execute(input: Id<Memory>) {
        memoryRepository.delete(input)
    }
}
