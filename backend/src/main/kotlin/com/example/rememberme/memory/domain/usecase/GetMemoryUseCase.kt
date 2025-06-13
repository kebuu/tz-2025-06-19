package com.example.rememberme.memory.domain.usecase

import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.memory.domain.spi.MemoryRepository
import com.example.rememberme.shared.domain.Id
import com.example.rememberme.shared.domain.usecase.UseCase

class GetMemoryUseCase(private val memoryRepository: MemoryRepository) : UseCase<Id<Memory>, Memory?> {
    override fun execute(input: Id<Memory>): Memory? {
        return memoryRepository.findById(input)
    }
}
