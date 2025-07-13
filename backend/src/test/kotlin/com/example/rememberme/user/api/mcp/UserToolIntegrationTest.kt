package com.example.rememberme.user.api.mcp

import com.example.rememberme.shared.test.IntegrationTest
import com.example.rememberme.user.api.mcp.UserTool.Companion.LIST_USERS_TOOL_NAME
import com.example.rememberme.user.infrastructure.persistence.model.DbUser
import com.example.rememberme.user.infrastructure.persistence.repository.JpaUserStore
import io.modelcontextprotocol.client.McpClient
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport
import io.modelcontextprotocol.spec.McpSchema
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID


class UserToolIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var jpaUserStore: JpaUserStore

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
    fun `should return all users when listUsers is called`() {
        val transport = HttpClientSseClientTransport
            .builder("http://localhost:$localServerPort")
            .sseEndpoint("/api/sse")
            .build()
        val client = McpClient.sync(transport).build()
        client.initialize()

        val callToolResult = client.callTool(McpSchema.CallToolRequest(LIST_USERS_TOOL_NAME, mapOf()))
        val responseString = (callToolResult.content[0] as McpSchema.TextContent).text
        // Extract and verify the result
        val resultList = objectMapper.readValue(responseString, Array<UserTool.UserDto>::class.java).toList()

        Assertions.assertThat(resultList).containsExactlyInAnyOrder(
            UserTool.UserDto(
                id = userId1.toString(),
                name = "user1",
                email = "user1@example.com"
            ),
            UserTool.UserDto(
                id = userId2.toString(),
                name = "user2",
                email = "user2@example.com"
            )
        )
    }
}
