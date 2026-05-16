package com.ulyup.tierlist.core.network.util

import kotlinx.coroutines.CancellationException

suspend fun <T> apiCall(call: suspend () -> T): T = try {
    call()
} catch (cancellation: CancellationException) {
    throw cancellation
} catch (apiException: ApiException) {
    throw apiException
} catch (throwable: Throwable) {
    throw ApiException.NetworkError(throwable.message ?: "Network error")
}