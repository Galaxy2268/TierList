package com.ulyup.tierlist.data.network.util

import com.ulyup.tierlist.domain.error.ApiException
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