package com.example.rememberme.memory.api

import com.example.rememberme.memory.api.dto.CreateMemoryRequestDto
import com.example.rememberme.memory.api.dto.MemoryDto
import com.example.rememberme.memory.api.dto.UpdateMemoryRequestDto
import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.memory.infrastructure.persistence.model.DbMemory
import com.example.rememberme.memory.infrastructure.persistence.model.DbMemoryUserLink
import com.example.rememberme.memory.infrastructure.persistence.store.JpaMemoryStore
import com.example.rememberme.shared.domain.Id
import com.example.rememberme.shared.test.IntegrationTest
import com.example.rememberme.user.infrastructure.persistence.model.DbUser
import com.example.rememberme.user.infrastructure.persistence.repository.JpaUserStore
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.request.RequestPostProcessor
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.util.Base64
import java.util.UUID

class MemoryControllerIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var jpaMemoryStore: JpaMemoryStore

    @Autowired
    private lateinit var jpaUserStore: JpaUserStore

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var memoryId1: UUID
    private lateinit var memoryId2: UUID
    private lateinit var userId: UUID
    private val today = LocalDate.now()
    private val yesterday = today.minusDays(1)

    // Helper method for Basic authentication
    private fun basicAuth(username: String, password: String): RequestPostProcessor {
        return RequestPostProcessor { request ->
            val base64Credentials = Base64.getEncoder().encodeToString("$username:$password".toByteArray())
            request.addHeader("Authorization", "Basic $base64Credentials")
            request
        }
    }

    private lateinit var otherUserId: UUID
    private lateinit var otherUserMemoryId: UUID

    @BeforeEach
    fun setup() {
        // Clean the database before each test
        jpaMemoryStore.deleteAll()
        jpaUserStore.deleteAll()

        // Create a test user
        userId = UUID.randomUUID()
        jpaUserStore.save(
            DbUser(
                id = userId,
                pseudo = "testUser",
                email = "test@example.com"
            )
        )

        // Create another test user
        otherUserId = UUID.randomUUID()
        jpaUserStore.save(
            DbUser(
                id = otherUserId,
                pseudo = "otherUser",
                email = "other@example.com"
            )
        )

        // Insert test data
        memoryId1 = UUID.randomUUID()
        memoryId2 = UUID.randomUUID()
        otherUserMemoryId = UUID.randomUUID()

        jpaMemoryStore.saveAll(
            listOf(
                DbMemory(
                    id = memoryId1,
                    text = "Memory 1 text",
                    day = today,
                    userId = userId,
                    userLinks = emptyList()
                ),
                DbMemory(
                    id = memoryId2,
                    text = "Memory 2 text",
                    day = yesterday,
                    userId = userId,
                    userLinks = emptyList()
                ),
                DbMemory(
                    id = otherUserMemoryId,
                    text = "Other user's memory",
                    day = today,
                    userId = otherUserId,
                    userLinks = emptyList()
                )
            )
        )
    }

    @Test
    fun `should return only user's memories when getAllMemories is called`() {
        // When
        val result = mockMvc.perform(
            get("/memories")
                .contentType(MediaType.APPLICATION_JSON)
                .with(basicAuth(userId.toString(), "zenika"))
        )
            // Then
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        // Parse the response content into a list of MemoryDto objects
        val memories: List<MemoryDto> = objectMapper.readValue(result.response.contentAsString)

        // Validate the response using direct object equality comparison
        // Should only contain the memories belonging to the authenticated user
        assertThat(memories).containsExactlyInAnyOrder(
            MemoryDto(
                id = Id.of<Memory>(memoryId1),
                text = "Memory 1 text",
                day = today
            ),
            MemoryDto(
                id = Id.of<Memory>(memoryId2),
                text = "Memory 2 text",
                day = yesterday
            )
        )

        // Verify that the other user's memory is not included
        assertThat(memories).noneMatch { it.id == Id.of<Memory>(otherUserMemoryId) }
    }

    @Test
    fun `should create a memory when createMemory is called`() {
        // Given
        val pastDate = today.minusDays(7) // Use a past date to avoid conflict with existing memories
        val createMemoryRequest = CreateMemoryRequestDto(
            text = "New memory text",
            day = pastDate
        )

        // When
        val result = mockMvc.perform(
            post("/memories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMemoryRequest))
                .with(basicAuth(userId.toString(), "zenika"))
        )
            // Then
            .andExpect(status().isCreated)
            .andReturn()

        // Get the location header from the response
        val locationHeader = checkNotNull(result.response.getHeader("Location"))

        // Verify the memory was saved to the database
        val memories = jpaMemoryStore.findAll()
        assertThat(memories.any { it.text == "New memory text" && it.day == pastDate && it.userId == userId }).isTrue()

        // Verify that the URI in the location header can be used to retrieve the newly created memory
        val getMemoryResult = mockMvc.perform(
            get(locationHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .with(basicAuth(userId.toString(), "zenika"))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        // Parse the response content into a MemoryDto object
        val memory: MemoryDto = objectMapper.readValue(getMemoryResult.response.contentAsString)

        // Validate the response using direct object equality comparison
        // We need to extract the ID from the memory object since it's generated dynamically
        assertThat(memory).isEqualTo(MemoryDto(
            id = memory.id,
            text = "New memory text",
            day = pastDate
        ))
    }

    @Test
    fun `should return a memory when getMemory is called with valid id`() {
        // When
        val result = mockMvc.perform(
            get("/memories/${memoryId1}")
                .contentType(MediaType.APPLICATION_JSON)
                .with(basicAuth(userId.toString(), "zenika"))
        )
            // Then
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        // Parse the response content into a MemoryDto object
        val memory: MemoryDto = objectMapper.readValue(result.response.contentAsString)

        // Validate the response using direct object equality comparison
        assertThat(memory).isEqualTo(MemoryDto(
            id = Id.of<Memory>(memoryId1),
            text = "Memory 1 text",
            day = today
        ))
    }

    @Test
    fun `should return 404 when getMemory is called with non-existent id`() {
        // Given
        val nonExistentId = UUID.randomUUID()

        // When
        mockMvc.perform(
            get("/memories/${nonExistentId}")
                .contentType(MediaType.APPLICATION_JSON)
                .with(basicAuth(userId.toString(), "zenika"))
        )
            // Then
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should return 404 when getMemory is called with id of memory belonging to another user`() {
        // When
        mockMvc.perform(
            get("/memories/${otherUserMemoryId}")
                .contentType(MediaType.APPLICATION_JSON)
                .with(basicAuth(userId.toString(), "zenika"))
        )
            // Then
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should update a memory when updateMemory is called with valid id`() {
        // Given
        val updateMemoryRequest = UpdateMemoryRequestDto(
            text = "Updated memory text",
            day = yesterday
        )

        // When
        mockMvc.perform(
            put("/memories/${memoryId1}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateMemoryRequest))
                .with(basicAuth(userId.toString(), "zenika"))
        )
            // Then
            .andExpect(status().isNoContent)

        // Verify the memory was updated in the database
        val updatedMemory = jpaMemoryStore.findMemoryById(memoryId1)
        assertThat(updatedMemory).isNotNull
        assertThat(updatedMemory?.text).isEqualTo("Updated memory text")
        assertThat(updatedMemory?.day).isEqualTo(yesterday)
    }

    @Test
    fun `should return 404 when updateMemory is called with non-existent id`() {
        // Given
        val nonExistentId = UUID.randomUUID()
        val updateMemoryRequest = UpdateMemoryRequestDto(
            text = "Updated memory text",
            day = yesterday
        )

        // When
        mockMvc.perform(
            put("/memories/${nonExistentId}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateMemoryRequest))
                .with(basicAuth(userId.toString(), "zenika"))
        )
            // Then
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should return 404 when updateMemory is called with id of memory belonging to another user`() {
        // Given
        val updateMemoryRequest = UpdateMemoryRequestDto(
            text = "Trying to update another user's memory",
            day = yesterday
        )

        // When
        mockMvc.perform(
            put("/memories/${otherUserMemoryId}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateMemoryRequest))
                .with(basicAuth(userId.toString(), "zenika"))
        )
            // Then
            .andExpect(status().isNotFound)

        // Verify the memory was not updated in the database
        val memory = jpaMemoryStore.findMemoryById(otherUserMemoryId)
        assertThat(memory).isNotNull
        assertThat(memory?.text).isEqualTo("Other user's memory")
        assertThat(memory?.day).isEqualTo(today)
    }

    @Test
    fun `should delete a memory when deleteMemory is called with valid id`() {
        // When
        mockMvc.perform(
            delete("/memories/${memoryId1}")
                .contentType(MediaType.APPLICATION_JSON)
                .with(basicAuth(userId.toString(), "zenika"))
        )
            // Then
            .andExpect(status().isNoContent)

        // Verify the memory was deleted from the database
        val deletedMemory = jpaMemoryStore.findMemoryById(memoryId1)
        assertThat(deletedMemory).isNull()

        // Verify that only one memory was deleted
        val remainingMemories = jpaMemoryStore.findAllByUserId(userId)
        assertThat(remainingMemories).hasSize(1)
        assertThat(remainingMemories[0].id).isEqualTo(memoryId2)

        // Verify that the other user's memory was not deleted
        val otherUserMemories = jpaMemoryStore.findAllByUserId(otherUserId)
        assertThat(otherUserMemories).hasSize(1)
        assertThat(otherUserMemories[0].id).isEqualTo(otherUserMemoryId)
    }

    @Test
    fun `should return 404 when deleteMemory is called with non-existent id`() {
        // Given
        val nonExistentId = UUID.randomUUID()

        // When
        mockMvc.perform(
            delete("/memories/${nonExistentId}")
                .contentType(MediaType.APPLICATION_JSON)
                .with(basicAuth(userId.toString(), "zenika"))
        )
            // Then
            .andExpect(status().isNotFound)

        // Verify that no memories were deleted
        val userMemories = jpaMemoryStore.findAllByUserId(userId)
        assertThat(userMemories).hasSize(2)

        // Verify that the other user's memory was not deleted
        val otherUserMemories = jpaMemoryStore.findAllByUserId(otherUserId)
        assertThat(otherUserMemories).hasSize(1)
    }

    @Test
    fun `should return 404 when deleteMemory is called with id of memory belonging to another user`() {
        // When
        mockMvc.perform(
            delete("/memories/${otherUserMemoryId}")
                .contentType(MediaType.APPLICATION_JSON)
                .with(basicAuth(userId.toString(), "zenika"))
        )
            // Then
            .andExpect(status().isNotFound)

        // Verify that the memory was not deleted from the database
        val memory = jpaMemoryStore.findMemoryById(otherUserMemoryId)
        assertThat(memory).isNotNull

        // Verify that no user memories were deleted
        val userMemories = jpaMemoryStore.findAllByUserId(userId)
        assertThat(userMemories).hasSize(2)

        // Verify that all other user memories are still there
        val otherUserMemories = jpaMemoryStore.findAllByUserId(otherUserId)
        assertThat(otherUserMemories).hasSize(1)
    }

    @Test
    fun `should return 401 when getAllMemories is called with non-existent user id`() {
        // Given
        val nonExistentUserId = UUID.randomUUID()

        // When
        mockMvc.perform(
            get("/memories")
                .contentType(MediaType.APPLICATION_JSON)
                .with(basicAuth(nonExistentUserId.toString(), "zenika"))
        )
            // Then
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `should return 401 when getAllMemories is called with incorrect password`() {
        // When
        mockMvc.perform(
            get("/memories")
                .contentType(MediaType.APPLICATION_JSON)
                .with(basicAuth(userId.toString(), "wrongpassword"))
        )
            // Then
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `should return linked memories when getLinkedMemories is called`() {
        // Given
        // Create a memory owned by otherUser and link it to the current user with access granted
        val linkedMemoryId = UUID.randomUUID()
        val linkedMemory = DbMemory(
            id = linkedMemoryId,
            text = "Linked memory text",
            day = today,
            userId = otherUserId,
            userLinks = listOf(
                DbMemoryUserLink(
                    userId = userId,
                    userCanAccess = true
                )
            )
        )
        jpaMemoryStore.save(linkedMemory)

        // Create another memory owned by otherUser but with access not granted to current user
        val noAccessMemoryId = UUID.randomUUID()
        val noAccessMemory = DbMemory(
            id = noAccessMemoryId,
            text = "No access memory text",
            day = today,
            userId = otherUserId,
            userLinks = listOf(
                DbMemoryUserLink(
                    userId = userId,
                    userCanAccess = false
                )
            )
        )
        jpaMemoryStore.save(noAccessMemory)

        // When
        val result = mockMvc.perform(
            get("/memories/linked")
                .contentType(MediaType.APPLICATION_JSON)
                .with(basicAuth(userId.toString(), "zenika"))
        )
            // Then
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        // Parse the response content into a list of MemoryDto objects
        val memories: List<MemoryDto> = objectMapper.readValue(result.response.contentAsString)

        // Validate the response
        // Should only contain the memory where the user is linked and has access granted
        assertThat(memories).hasSize(1)
        assertThat(memories[0].id.asString()).isEqualTo(linkedMemoryId.toString())
        assertThat(memories[0].text).isEqualTo("Linked memory text")
        assertThat(memories[0].day).isEqualTo(today)

        // Verify that the memory where the user is linked but doesn't have access is not included
        assertThat(memories).noneMatch { it.id.asString() == noAccessMemoryId.toString() }
    }

    @Test
    fun `should return 409 when trying to create a memory with same date and owner as existing memory`() {
        // Given
        val createMemoryRequest = CreateMemoryRequestDto(
            text = "Duplicate memory text",
            day = today // Same day as memoryId1
        )

        // When
        mockMvc.perform(
            post("/memories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMemoryRequest))
                .with(basicAuth(userId.toString(), "zenika"))
        )
            // Then
            .andExpect(status().isConflict)
            .andReturn()

        // No need to verify the response content as it's empty

        // Verify no new memory was created
        val memories = jpaMemoryStore.findAll()
        assertThat(memories.none { it.text == "Duplicate memory text" }).isTrue()
    }
}
