package com.example.rememberme.user.api

import com.example.rememberme.user.infrastructure.persistence.model.DbUser
import com.example.rememberme.user.infrastructure.persistence.repository.JpaUserStore
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var jpaUserStore: JpaUserStore

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        // Clean the database before each test
        jpaUserStore.deleteAll()

        // Insert test data
        val userId1 = UUID.randomUUID()
        val userId2 = UUID.randomUUID()

        jpaUserStore.saveAll(
            listOf(
                DbUser(
                    id = userId1,
                    email = "user1@example.com",
                    pseudo = "user1"
                ),
                DbUser(
                    id = userId2,
                    email = "user2@example.com",
                    pseudo = "user2"
                )
            )
        )
    }

    @Test
    fun `should return all users when getAllUsers is called`() {
        // When
        mockMvc.perform(
            get("/users")
                .contentType(MediaType.APPLICATION_JSON)
        )
            // Then
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].email").exists())
            .andExpect(jsonPath("$[0].pseudo").exists())
            .andExpect(jsonPath("$[1].id").exists())
            .andExpect(jsonPath("$[1].email").exists())
            .andExpect(jsonPath("$[1].pseudo").exists())
    }

    @Test
    fun `should create a user when createUser is called`() {
        // Given
        val createUserRequest = CreateUserRequestDto(
            email = "newuser@example.com",
            pseudo = "newuser"
        )

        // When
        mockMvc.perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest))
        )
            // Then
            .andExpect(status().isCreated)

        // Verify the user was saved to the database
        val users = jpaUserStore.findAll()
        assert(users.size == 3)
        assert(users.any { it.email == "newuser@example.com" && it.pseudo == "newuser" })
    }
}
