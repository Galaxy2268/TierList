package com.ulyup.tier_list.data.network.util

import com.ulyup.tier_list.domain.error.ApiException
import com.ulyup.tier_list.dto.ErrorResponse
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

suspend fun HttpResponse.toApiException(): ApiException {
    val message = readErrorMessage()
    return when (status) {
        HttpStatusCode.BadRequest -> ApiException.BadRequest(message)
        HttpStatusCode.Unauthorized -> ApiException.Unauthorized(message)
        HttpStatusCode.Forbidden -> ApiException.Forbidden(message)
        HttpStatusCode.NotFound -> ApiException.NotFound(message)
        HttpStatusCode.Conflict -> ApiException.Conflict(message)
        else -> if (status.value in 500..599) {
            ApiException.ServerError(message)
        } else {
            ApiException.Unknown(message)
        }
    }
}

private suspend fun HttpResponse.readErrorMessage(): String =
    runCatching { body<ErrorResponse>().message }
        .getOrElse { status.description }