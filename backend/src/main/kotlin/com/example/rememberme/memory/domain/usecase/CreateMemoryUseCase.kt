package com.example.rememberme.memory.domain.usecase

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.memory.domain.spi.MemoryRepository
import com.example.rememberme.shared.domain.usecase.UseCase

sealed class CreateMemoryError
object MemoryAlreadyExists : CreateMemoryError()

class CreateMemoryUseCase(
    private val memoryRepository: MemoryRepository
) : UseCase<Memory, Either<CreateMemoryError, Unit>> {
    override fun execute(input: Memory): Either<CreateMemoryError, Unit> {
        return either {
            ensure(!memoryRepository.existsByDayAndOwnerId(input.day.value, input.ownerId)) { 
                MemoryAlreadyExists 
            }
            memoryRepository.save(input)
        }
    }
}
