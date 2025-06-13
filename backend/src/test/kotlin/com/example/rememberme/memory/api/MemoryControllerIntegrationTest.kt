package com.example.rememberme.memory.api

import com.example.rememberme.memory.api.dto.CreateMemoryRequestDto
import com.example.rememberme.memory.api.dto.MemoryDto
import com.example.rememberme.memory.api.dto.UpdateMemoryRequestDto
import com.example.rememberme.memory.domain.Memory
import com.example.rememberme.memory.infrastructure.persistence.model.DbMemory
import com.example.rememberme.memory.infrastructure.persistence.repository.JpaMemoryStore
import com.example.rememberme.shared.domain.Id
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MemoryControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var jpaMemoryStore: JpaMemoryStore

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var memoryId1: UUID
    private lateinit var memoryId2: UUID
    private val today = LocalDate.now()
    private val yesterday = today.minusDays(1)

    @BeforeEach
    fun setup() {
        // Clean the database before each test
        jpaMemoryStore.deleteAll()

        // Insert test data
        memoryId1 = UUID.randomUUID()
        memoryId2 = UUID.randomUUID()

        jpaMemoryStore.saveAll(
            listOf(
                DbMemory(
                    id = memoryId1,
                    text = "Memory 1 text",
                    day = today
                ),
                DbMemory(
                    id = memoryId2,
                    text = "Memory 2 text",
                    day = yesterday
                )
            )
        )
    }

    @Test
    fun `should return all memories when getAllMemories is called`() {
        // When
        val result = mockMvc.perform(
            get("/memories")
                .contentType(MediaType.APPLICATION_JSON)
        )
            // Then
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        // Parse the response content into a list of MemoryDto objects
        val memories: List<MemoryDto> = objectMapper.readValue(result.response.contentAsString)

        // Validate the response using direct object equality comparison
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
    }

    @Test
    fun `should create a memory when createMemory is called`() {
        // Given
        val createMemoryRequest = CreateMemoryRequestDto(
            text = "New memory text",
            day = today
        )

        // When
        val result = mockMvc.perform(
            post("/memories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMemoryRequest))
        )
            // Then
            .andExpect(status().isCreated)
            .andReturn()

        // Get the location header from the response
        val locationHeader = checkNotNull(result.response.getHeader("Location"))

        // Verify the memory was saved to the database
        val memories = jpaMemoryStore.findAll()
        assertThat(memories).hasSize(3)
        assertThat(memories.any { it.text == "New memory text" && it.day == today }).isTrue()

        // Verify that the URI in the location header can be used to retrieve the newly created memory
        val getMemoryResult = mockMvc.perform(
            get(locationHeader)
                .contentType(MediaType.APPLICATION_JSON)
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
            day = today
        ))
    }

    @Test
    fun `should return a memory when getMemory is called with valid id`() {
        // When
        val result = mockMvc.perform(
            get("/memories/${memoryId1}")
                .contentType(MediaType.APPLICATION_JSON)
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
        )
            // Then
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should delete a memory when deleteMemory is called with valid id`() {
        // When
        mockMvc.perform(
            delete("/memories/${memoryId1}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            // Then
            .andExpect(status().isNoContent)

        // Verify the memory was deleted from the database
        val deletedMemory = jpaMemoryStore.findMemoryById(memoryId1)
        assertThat(deletedMemory).isNull()

        // Verify that only one memory was deleted
        val remainingMemories = jpaMemoryStore.findAll()
        assertThat(remainingMemories).hasSize(1)
        assertThat(remainingMemories[0].id).isEqualTo(memoryId2)
    }

    @Test
    fun `should return 204 when deleteMemory is called with non-existent id`() {
        // Given
        val nonExistentId = UUID.randomUUID()

        // When
        mockMvc.perform(
            delete("/memories/${nonExistentId}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            // Then
            .andExpect(status().isNoContent)

        // Verify that no memories were deleted
        val remainingMemories = jpaMemoryStore.findAll()
        assertThat(remainingMemories).hasSize(2)
    }
}
