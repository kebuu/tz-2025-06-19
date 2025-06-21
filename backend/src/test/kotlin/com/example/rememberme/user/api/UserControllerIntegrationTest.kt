package com.example.rememberme.user.api

import com.example.rememberme.shared.domain.Id
import com.example.rememberme.shared.test.IntegrationTest
import com.example.rememberme.user.api.dto.CreateUserRequestDto
import com.example.rememberme.user.api.dto.UserDto
import com.example.rememberme.user.domain.Email
import com.example.rememberme.user.domain.Pseudo
import com.example.rememberme.user.domain.User
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

class UserControllerIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var jpaUserStore: JpaUserStore

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var userId1: UUID
    private lateinit var userId2: UUID

    @BeforeEach
    fun setup() {
        // Clean the database before each test
        jpaUserStore.deleteAll()

        // Insert test data
        userId1 = UUID.randomUUID()
        userId2 = UUID.randomUUID()

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
        val result = mockMvc.perform(
            get("/users")
                .contentType(MediaType.APPLICATION_JSON)
        )
            // Then
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        // Parse the response content into a list of UserDto objects
        val users: List<UserDto> = objectMapper.readValue(result.response.contentAsString)

        // Validate the response using direct object equality comparison
        assertThat(users).containsExactlyInAnyOrder(
            UserDto(
                id = Id.of<User>(userId1),
                email = Email("user1@example.com"),
                pseudo = Pseudo("user1")
            ),
            UserDto(
                id = Id.of<User>(userId2),
                email = Email("user2@example.com"),
                pseudo = Pseudo("user2")
            )
        )
    }

    @Test
    fun `should create a user when createUser is called`() {
        // Given
        val createUserRequest = CreateUserRequestDto(
            email = "newuser@example.com",
            pseudo = "newuser"
        )

        // When
        val result = mockMvc.perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest))
        )
            // Then
            .andExpect(status().isCreated)
            .andReturn()

        // Get the location header from the response
        val locationHeader = checkNotNull(result.response.getHeader("Location"))

        // Verify the user was saved to the database
        val users = jpaUserStore.findAll()
        assert(users.size == 3)
        assert(users.any { it.email == "newuser@example.com" && it.pseudo == "newuser" })

        // Verify that the URI in the location header can be used to retrieve the newly created user
        val getUserResult = mockMvc.perform(
            get(locationHeader)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        // Parse the response content into a UserDto object
        val user: UserDto = objectMapper.readValue(getUserResult.response.contentAsString)

        // Validate the response using direct object equality comparison
        // We need to extract the ID from the user object since it's generated dynamically
        assertThat(user).isEqualTo(UserDto(
            id = user.id,
            email = Email("newuser@example.com"),
            pseudo = Pseudo("newuser")
        ))
    }

    @Test
    fun `should return a user when getUser is called with valid id`() {
        // When
        val result = mockMvc.perform(
            get("/users/${userId1}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            // Then
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        // Parse the response content into a UserDto object
        val user: UserDto = objectMapper.readValue(result.response.contentAsString)

        // Validate the response using direct object equality comparison
        assertThat(user).isEqualTo(UserDto(
            id = Id.of<User>(userId1),
            email = Email("user1@example.com"),
            pseudo = Pseudo("user1")
        ))
    }

    @Test
    fun `should return 404 when getUser is called with non-existent id`() {
        // Given
        val nonExistentId = UUID.randomUUID()

        // When
        mockMvc.perform(
            get("/users/${nonExistentId}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            // Then
            .andExpect(status().isNotFound)
    }
}
