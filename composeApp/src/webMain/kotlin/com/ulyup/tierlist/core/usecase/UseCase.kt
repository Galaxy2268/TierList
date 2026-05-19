package com.ulyup.tierlist.core.usecase

import com.ulyup.tierlist.core.network.util.ApiException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class UseCase<in P, out R> {

    operator fun invoke(parameters: P): Flow<Resource<R>> = flow {
        emit(LoadingResource)
        try {
            emit(SuccessResource(execute(parameters)))
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (apiException: ApiException) {
            emit(ErrorResource(apiException))
        }
    }

    protected abstract suspend fun execute(parameters: P): R
}