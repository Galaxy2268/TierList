package com.ulyup.tier_list.data.network.util

import com.ulyup.tier_list.domain.error.ApiException
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