package com.ulyup.tier_list.core.usecase

import com.ulyup.tier_list.domain.error.ApiException
import kotlinx.coroutines.flow.Flow

sealed interface Resource<out T>

data object LoadingResource : Resource<Nothing>

value class SuccessResource<T>(val data: T) : Resource<T>

value class ErrorResource(val exception: ApiException) : Resource<Nothing>

suspend fun <T> Flow<Resource<T>>.fold(
    onLoading: () -> Unit = {},
    onSuccess: (T) -> Unit = {},
    onError: (ApiException) -> Unit = {},
) = collect { resource ->
    when (resource) {
        LoadingResource -> onLoading()
        is SuccessResource -> onSuccess(resource.data)
        is ErrorResource -> onError(resource.exception)
    }
}
