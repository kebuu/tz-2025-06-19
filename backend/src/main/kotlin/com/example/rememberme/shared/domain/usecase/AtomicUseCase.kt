package com.example.rememberme.shared.domain.usecase

/**
 * Represents a specialized use case, ensuring that its execution occurs as a discrete,
 * indivisible operation. An implementation of this interface should guarantee atomicity,
 * ensuring that operations are completed fully or not at all.
 *
 * This interface extends the generic UseCase interface, inheriting its input-output contract,
 * and is meant to be used in scenarios where atomic behavior is critical for correctness.
 *
 * @param I the input type for the use case
 * @param O the output type for the use case
 */
interface AtomicUseCase<I, O>: UseCase<I, O>
