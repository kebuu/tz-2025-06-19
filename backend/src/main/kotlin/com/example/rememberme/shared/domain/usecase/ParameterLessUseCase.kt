package com.example.rememberme.shared.domain.usecase

/**
 * A specialized use case interface for scenarios where the execution does not require any input parameters.
 * This interface extends the generic UseCase interface with the same input and output contract but
 * provides an additional parameter-less execution method for convenience.
 *
 * The `execute()` method should handle the operation without requiring an explicit input, and the default
 * implementation of `execute(input: I)` redirects to `execute()` to support compatibility with the base UseCase interface.
 *
 * @param O the output type for the use case
 */
interface ParameterLessUseCase<O>: UseCase<Unit, O> {
    override fun execute(input: Unit): O = execute()
    fun execute(): O
}
