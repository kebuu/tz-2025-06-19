package com.example.rememberme.shared.infrastructure.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionOperations

/**
 * Aspect that handles transactions for all methods in classes implementing
 * [com.example.rememberme.shared.domain.usecase.AtomicUseCase].
 * It starts a transaction before the execute method is called and commits or rolls back
 * the transaction after the method completes or throws an exception.
 */
@Aspect
@Component
class AtomicUseCaseAspect(
    private val transactionOperations: TransactionOperations
) {

    /**
     * Pointcut that matches all methods in classes implementing
     * [com.example.rememberme.shared.domain.usecase.AtomicUseCase]
     */
    @Pointcut("execution(* *.*(..)) && target(com.example.rememberme.shared.domain.usecase.AtomicUseCase)")
    fun transactionalUseCaseExecution() {
    }

    /**
     * Advice that wraps the execution of the matched methods in a transaction.
     *
     * @param joinPoint the join point representing the intercepted method
     * @return the result of the method execution
     * @throws Throwable if the method execution throws an exception
     */
    @Around("transactionalUseCaseExecution()")
    fun aroundTransactionalUseCaseExecution(joinPoint: ProceedingJoinPoint): Any? {
        return transactionOperations.execute { status ->
            try {
                joinPoint.proceed()
            } catch (e: Throwable) {
                status.setRollbackOnly()
                throw e
            }
        }
    }
}
