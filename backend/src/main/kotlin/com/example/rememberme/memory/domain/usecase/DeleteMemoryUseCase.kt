package com.example.rememberme.memory.domain.usecase

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.memory.domain.spi.MemoryRepository
import com.example.rememberme.shared.domain.Id
import com.example.rememberme.shared.domain.ResourceNotFound
import com.example.rememberme.shared.domain.usecase.UseCase
import com.example.rememberme.user.domain.User

class DeleteMemoryUseCase(
    private val memoryRepository: MemoryRepository
) : UseCase<DeleteMemoryInput, Either<ResourceNotFound, Unit>> {
    override fun execute(input: DeleteMemoryInput): Either<ResourceNotFound, Unit> {
        return either {
            val existingMemory = ensureNotNull(memoryRepository.findById(input.memoryId)) { ResourceNotFound }
            ensure(existingMemory.hasOwnerId(input.userId)) { ResourceNotFound }
            memoryRepository.delete(input.memoryId)
        }
    }
}

data class DeleteMemoryInput(
    val memoryId: Id<Memory>,
    val userId: Id<User>
)
