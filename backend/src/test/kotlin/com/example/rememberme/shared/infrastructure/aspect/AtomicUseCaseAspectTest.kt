package com.example.rememberme.shared.infrastructure.aspect

import com.example.rememberme.shared.domain.usecase.AtomicUseCase
import com.example.rememberme.shared.test.IntegrationTest
import com.example.rememberme.user.infrastructure.persistence.model.DbUser
import com.example.rememberme.user.infrastructure.persistence.repository.JpaUserStore
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.UUID

// Data classes and exception used by the test
data class TestInput(val email: String, val pseudo: String)
data class TestOutput(val userId: UUID)
class TestException(message: String) : RuntimeException(message)

@Component
class TestAtomicUseCase(
    private val jpaUserStore: JpaUserStore
) : AtomicUseCase<TestInput, TestOutput> {

    override fun execute(input: TestInput): TestOutput {
        // Create and save a user to the database
        val userId = UUID.randomUUID()
        val dbUser = DbUser(
            id = userId,
            email = input.email,
            pseudo = input.pseudo
        )

        jpaUserStore.save(dbUser)

        // Verify the user was saved
        val savedUser = jpaUserStore.findById(userId).orElse(null)
        if (savedUser == null) {
            throw IllegalStateException("User was not saved to the database")
        }

        // Throw an exception to trigger transaction rollback
        throw TestException("Test exception to trigger rollback")
    }
}

class AtomicUseCaseAspectTest : IntegrationTest() {

    @Autowired
    private lateinit var jpaUserStore: JpaUserStore

    @Autowired
    private lateinit var testAtomicUseCase: TestAtomicUseCase

    @BeforeEach
    fun setup() {
        // Ensure the database is clean before each test
        jpaUserStore.deleteAll()
    }

    @Test
    fun `should rollback database changes when exception is thrown`() {
        // Given
        assertThat(jpaUserStore.findAll()).isEmpty()

        // When
        val exception = assertThrows<TestException> {
            testAtomicUseCase.execute(TestInput("test@example.com", "testUser"))
        }

        // Then
        assertThat(exception.message).isEqualTo("Test exception to trigger rollback")

        // Verify that no users were saved to the database (changes were rolled back)
        assertThat(jpaUserStore.findAll()).isEmpty()
    }
}
