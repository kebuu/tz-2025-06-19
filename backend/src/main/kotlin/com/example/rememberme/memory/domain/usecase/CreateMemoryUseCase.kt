package com.example.rememberme.memory.domain.usecase

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.memory.domain.spi.MemoryRepository
import com.example.rememberme.memory.domain.MemoryAlreadyExists
import com.example.rememberme.shared.domain.usecase.UseCase

class CreateMemoryUseCase(
    private val memoryRepository: MemoryRepository
) : UseCase<Memory, Either<MemoryAlreadyExists, Unit>> {
    override fun execute(input: Memory): Either<MemoryAlreadyExists, Unit> {
        return either {
            ensure(!memoryRepository.existsByDayAndOwnerId(input.day, input.ownerId)) { 
                MemoryAlreadyExists 
            }
            memoryRepository.save(input)
        }
    }
}
