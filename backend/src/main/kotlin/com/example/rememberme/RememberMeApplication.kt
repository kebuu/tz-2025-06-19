package com.example.rememberme

import com.example.rememberme.shared.domain.annotation.UseCase
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter

@SpringBootApplication
class RememberMeApplication {
    @Bean
    fun useCaseComponentScanner(): ClassPathScanningCandidateComponentProvider {
        return ClassPathScanningCandidateComponentProvider(false)
            .apply { addIncludeFilter(AnnotationTypeFilter(UseCase::class.java)) }
    }
}

fun main(args: Array<String>) {
    runApplication<RememberMeApplication>(*args)
}
