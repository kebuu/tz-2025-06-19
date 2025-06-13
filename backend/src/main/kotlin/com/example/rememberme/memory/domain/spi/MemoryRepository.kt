package com.example.rememberme.memory.domain.spi

import com.example.rememberme.shared.domain.Id
import com.example.rememberme.memory.domain.Memory

interface MemoryRepository {

    fun findAll() : List<Memory>

    fun findById(id: Id<Memory>): Memory?

    fun save(memory: Memory)
    
    fun delete(id: Id<Memory>)
}
