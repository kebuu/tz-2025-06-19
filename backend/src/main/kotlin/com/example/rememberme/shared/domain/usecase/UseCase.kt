package com.example.rememberme.shared.domain.usecase

/**
 * Generic interface for all use cases in the application.
 * Each use case should implement this interface with appropriate input and output types.
 *
 * @param I the input type for the use case
 * @param O the output type for the use case
 */
interface UseCase<I, O> {
    /**
     * Executes the use case with the given input.
     *
     * @param input the input for the use case
     * @return the output of the use case
     */
    fun execute(input: I): O
}
