package com.ulyup.tierlist.core.network

import com.ulyup.tierlist.core.network.session.SessionManager
import com.ulyup.tierlist.core.network.util.API_BASE_URL
import com.ulyup.tierlist.core.network.util.toApiException
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    fun create(json: Json, sessionManager: SessionManager): HttpClient = HttpClient {
        expectSuccess = false

        install(ContentNegotiation) {
            json(json)
        }

        defaultRequest {
            url(API_BASE_URL)
            contentType(ContentType.Application.Json)
        }

        HttpResponseValidator {
            validateResponse { response: HttpResponse ->
                if (response.status.isSuccess()) return@validateResponse
                if (response.status == HttpStatusCode.Unauthorized) {
                    sessionManager.unauthorize()
                }
                throw response.toApiException()
            }
        }
    }
}