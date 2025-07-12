package com.example.rememberme

import com.example.rememberme.shared.domain.usecase.UseCase
import com.example.rememberme.user.api.mcp.UserTool
import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner
import org.springframework.core.type.filter.AssignableTypeFilter


@SpringBootApplication
class RememberMeApplication {
    @Bean
    fun useCaseRegistryPostProcessor(): BeanDefinitionRegistryPostProcessor {
        return BeanDefinitionRegistryPostProcessor { registry ->
            val scanner = ClassPathBeanDefinitionScanner(registry, false)
            scanner.addIncludeFilter(AssignableTypeFilter(UseCase::class.java))
            scanner.scan(
                "com.example.rememberme.user.domain.usecase",
                "com.example.rememberme.memory.domain.usecase"
            )
        }
    }

    @Bean
    fun tools(userTool: UserTool): ToolCallbackProvider {
        return MethodToolCallbackProvider.builder().toolObjects(userTool).build()
    }
}

fun main(args: Array<String>) {
    runApplication<RememberMeApplication>(*args)
}
