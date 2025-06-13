package com.example.rememberme.memory.api.controller

import com.example.rememberme.memory.api.dto.CreateMemoryRequestDto
import com.example.rememberme.memory.api.dto.MemoryDto
import com.example.rememberme.memory.api.dto.UpdateMemoryRequestDto
import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.memory.domain.MemoryText
import com.example.rememberme.memory.domain.usecase.CreateMemoryUseCase
import com.example.rememberme.memory.domain.usecase.DeleteMemoryUseCase
import com.example.rememberme.memory.domain.usecase.GetMemoriesUseCase
import com.example.rememberme.memory.domain.usecase.GetMemoryUseCase
import com.example.rememberme.memory.domain.usecase.UpdateMemoryUseCase
import com.example.rememberme.shared.domain.Id
import com.example.rememberme.user.domain.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.util.UUID
import kotlin.reflect.jvm.javaMethod

@RestController
@RequestMapping("/memories")
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
class MemoryController(
    val memoriesUseCase: GetMemoriesUseCase,
    val getMemoryUseCase: GetMemoryUseCase,
    val createMemoryUseCase: CreateMemoryUseCase,
    val updateMemoryUseCase: UpdateMemoryUseCase,
    val deleteMemoryUseCase: DeleteMemoryUseCase
) {

    @GetMapping
    fun getAllMemories(@AuthenticationPrincipal userDetails: UserDetails): List<MemoryDto> {
        val userId = UUID.fromString(userDetails.username)
        return memoriesUseCase.findByUserId(Id.of<User>(userId)).map { memory ->
            MemoryDto(
                id = memory.id,
                text = memory.text.value,
                day = memory.day
            )
        }
    }

    @GetMapping("/{id}")
    fun getMemory(@PathVariable id: UUID): ResponseEntity<MemoryDto> {
        return getMemoryUseCase.findById(Id.of(id))
            ?.let { memory ->
                ResponseEntity.ok(
                    MemoryDto(
                        id = memory.id,
                        text = memory.text.value,
                        day = memory.day
                    )
                )
            }
            ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createMemory(
        @RequestBody request: CreateMemoryRequestDto,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<Void> {
        val userId = UUID.fromString(userDetails.username)
        val newMemory = Memory(
            id = Id.random(),
            text = MemoryText(request.text),
            day = request.day,
            userId = Id.of<User>(userId)
        )
        createMemoryUseCase.create(newMemory)

        return ResponseEntity.created(
            MvcUriComponentsBuilder
                .fromMethod(
                    MemoryController::class.java,
                    MemoryController::getMemory.javaMethod!!,
                    newMemory.id.asString()
                )
                .build()
                .toUri()
        ).build()
    }

    @PutMapping("/{id}")
    fun updateMemory(
        @PathVariable id: UUID,
        @RequestBody request: UpdateMemoryRequestDto,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<Void> {
        return getMemoryUseCase.findById(Id.of<Memory>(id))?.let { existingMemory ->
            val userId = UUID.fromString(userDetails.username)
            val updatedMemory = Memory(
                id = existingMemory.id,
                text = MemoryText(request.text),
                day = request.day,
                userId = Id.of<User>(userId)
            )
            updateMemoryUseCase.update(updatedMemory)
            ResponseEntity.noContent().build()
        }
        ?: ResponseEntity.notFound().build()



    }

    @DeleteMapping("/{id}")
    fun deleteMemory(
        @PathVariable id: UUID,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<Void> {
        deleteMemoryUseCase.delete(Id.of<Memory>(id))
        return ResponseEntity.noContent().build()
    }
}
