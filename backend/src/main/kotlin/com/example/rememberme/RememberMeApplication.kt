package com.example.rememberme

import com.example.rememberme.shared.domain.annotation.UseCase
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner
import org.springframework.core.type.filter.AnnotationTypeFilter

@SpringBootApplication
class RememberMeApplication {
    @Bean
    fun useCaseRegistryPostProcessor(): BeanDefinitionRegistryPostProcessor {
        return BeanDefinitionRegistryPostProcessor { registry ->
            val scanner = ClassPathBeanDefinitionScanner(registry, false)
            scanner.addIncludeFilter(AnnotationTypeFilter(UseCase::class.java))
            scanner.scan(
                "com.example.rememberme.user.domain.usecase",
                "com.example.rememberme.memory.domain.usecase"
            )
        }
    }
}

fun main(args: Array<String>) {
    runApplication<RememberMeApplication>(*args)
}
