package com.example.rememberme.memory.api.controller

import com.example.rememberme.memory.api.dto.CreateMemoryRequestDto
import com.example.rememberme.memory.api.dto.MemoryDto
import com.example.rememberme.memory.api.dto.UpdateMemoryRequestDto
import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.memory.domain.MemoryText
import com.example.rememberme.memory.domain.usecase.CreateMemoryUseCase
import com.example.rememberme.memory.domain.usecase.DeleteMemoryInput
import com.example.rememberme.memory.domain.usecase.DeleteMemoryUseCase
import com.example.rememberme.memory.domain.usecase.GetMemoriesUseCase
import com.example.rememberme.memory.domain.usecase.GetMemoryInput
import com.example.rememberme.memory.domain.usecase.GetMemoryUseCase
import com.example.rememberme.memory.domain.usecase.UpdateMemoryUseCase
import com.example.rememberme.shared.domain.Id
import com.example.rememberme.user.domain.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "Memory", description = "Memory management API")
class MemoryController(
    val memoriesUseCase: GetMemoriesUseCase,
    val getMemoryUseCase: GetMemoryUseCase,
    val createMemoryUseCase: CreateMemoryUseCase,
    val updateMemoryUseCase: UpdateMemoryUseCase,
    val deleteMemoryUseCase: DeleteMemoryUseCase
) {

    @GetMapping
    @Operation(summary = "Get all memories", description = "Retrieves all memories for the authenticated user")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved memories",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = MemoryDto::class))])
    ])
    fun getAllMemories(@AuthenticationPrincipal userDetails: UserDetails): List<MemoryDto> {
        val userId = UUID.fromString(userDetails.username)
        return memoriesUseCase.execute(Id.of<User>(userId)).map { memory ->
            MemoryDto(
                id = memory.id,
                text = memory.text.value,
                day = memory.day
            )
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a memory by ID", description = "Retrieves a specific memory by its ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved the memory",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = MemoryDto::class))]),
        ApiResponse(responseCode = "404", description = "Memory not found", content = [Content()])
    ])
    fun getMemory(
        @Parameter(description = "ID of the memory to retrieve") @PathVariable id: UUID,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<MemoryDto> {
        val userId = UUID.fromString(userDetails.username)
        val input = GetMemoryInput(
            memoryId = Id.of(id),
            userId = Id.of<User>(userId)
        )
        return getMemoryUseCase.execute(input)
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
    @Operation(summary = "Create a new memory", description = "Creates a new memory for the authenticated user")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Memory successfully created", content = [Content()]),
        ApiResponse(responseCode = "400", description = "Invalid input", content = [Content()])
    ])
    fun createMemory(
        @Parameter(description = "Memory to create", required = true) 
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
        createMemoryUseCase.execute(newMemory)

        // Create a URI for the new memory
        val uri = MvcUriComponentsBuilder
            .fromMethod(
                MemoryController::class.java,
                MemoryController::getMemory.javaMethod!!,
                newMemory.id.asString(),
                userDetails
            )
            .build()
            .toUri()

        return ResponseEntity.created(uri).build()
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a memory", description = "Updates an existing memory")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Memory successfully updated", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Memory not found", content = [Content()]),
        ApiResponse(responseCode = "400", description = "Invalid input", content = [Content()])
    ])
    fun updateMemory(
        @Parameter(description = "ID of the memory to update") @PathVariable id: UUID,
        @Parameter(description = "Updated memory data", required = true) @RequestBody request: UpdateMemoryRequestDto,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<Void> {
        val userId = Id.of<User>(UUID.fromString(userDetails.username))
        val input = GetMemoryInput(
            memoryId = Id.of(id),
            userId = userId
        )
        return getMemoryUseCase.execute(input)?.let { existingMemory ->
            val updatedMemory = Memory(
                id = existingMemory.id,
                text = MemoryText(request.text),
                day = request.day,
                userId = userId
            )
            updateMemoryUseCase.execute(updatedMemory)
            ResponseEntity.noContent().build()
        }
        ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a memory", description = "Deletes an existing memory")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Memory successfully deleted", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Memory not found", content = [Content()])
    ])
    fun deleteMemory(
        @Parameter(description = "ID of the memory to delete") @PathVariable id: UUID,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<Void> {
        val userId = UUID.fromString(userDetails.username)
        val userIdObj = Id.of<User>(userId)
        val input = DeleteMemoryInput(
            memoryId = Id.of(id),
            userId = userIdObj
        )

        return deleteMemoryUseCase.execute(input)
            .fold(
                ifLeft = { ResponseEntity.notFound().build() } ,
                ifRight = { ResponseEntity.noContent().build() }
            )
    }
}
