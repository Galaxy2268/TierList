package com.ulyup.tier_list.core.usecase

import kotlinx.coroutines.flow.Flow

abstract class ObserveUseCase<in P, out R> {

    operator fun invoke(parameters: P): Flow<R> = execute(parameters)

    protected abstract fun execute(parameters: P): Flow<R>
}