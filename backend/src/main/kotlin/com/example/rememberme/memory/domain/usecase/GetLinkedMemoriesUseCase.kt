package com.example.rememberme.memory.domain.usecase

import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.memory.domain.spi.MemoryRepository
import com.example.rememberme.memory.domain.spi.MemoryRepository.MemoryRepositoryFilter.LinkedUsedWithViewGrantedAccess
import com.example.rememberme.shared.domain.Id
import com.example.rememberme.shared.domain.usecase.UseCase
import com.example.rememberme.user.domain.User

class GetLinkedMemoriesUseCase(val memoryRepository: MemoryRepository) : UseCase<Id<User>, List<Memory>> {
    override fun execute(input: Id<User>): List<Memory> {
        return memoryRepository.findAllFilteredBy(
            LinkedUsedWithViewGrantedAccess(input)
        )
    }
}
