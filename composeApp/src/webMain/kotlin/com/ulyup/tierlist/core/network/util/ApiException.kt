package com.ulyup.tierlist.core.network.util

sealed class ApiException(message: String) : RuntimeException(message) {
    class NetworkError(message: String) : ApiException(message)
    class Unauthorized(message: String) : ApiException(message)
    class Forbidden(message: String) : ApiException(message)
    class NotFound(message: String) : ApiException(message)
    class Conflict(message: String) : ApiException(message)
    class BadRequest(message: String) : ApiException(message)
    class ServerError(message: String) : ApiException(message)
    class Unknown(message: String) : ApiException(message)
}