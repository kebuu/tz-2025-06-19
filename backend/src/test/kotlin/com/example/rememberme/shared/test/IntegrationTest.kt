package com.example.rememberme.shared.test

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.RequestPostProcessor
import java.util.Base64

/**
 * Base class for integration tests that use MockMvc.
 * Contains common annotations for Spring Boot integration tests.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
abstract class IntegrationTest {

    @LocalServerPort
    lateinit var localServerPort: Integer

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    fun assertFieldHasError(
        mvcResult: MvcResult,
        fieldName: String
    ) {
        // Parse the response and verify it contains an error for the given field
        val errors: Map<String, String> = objectMapper.readValue(mvcResult.response.contentAsString)
        Assertions.assertThat(errors).containsKey(fieldName)
        Assertions.assertThat(errors[fieldName]).isNotEmpty()
    }

    // Helper method for Basic authentication
    fun basicAuth(
        username: String,
        password: String
    ): RequestPostProcessor {
        return RequestPostProcessor { request ->
            val base64Credentials = Base64.getEncoder().encodeToString("$username:$password".toByteArray())
            request.addHeader("Authorization", "Basic $base64Credentials")
            request
        }
    }
}

