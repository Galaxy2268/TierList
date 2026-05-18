package com.ulyup.tierlist.core.network.session

import com.ulyup.tierlist.Routes
import com.ulyup.tierlist.core.network.util.ApiException
import com.ulyup.tierlist.core.network.util.apiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class SessionBootstrapper(
    private val httpClient: HttpClient,
    private val sessionManager: SessionManager,
) {

    suspend fun bootstrap() {
        try {
            apiCall { httpClient.get(Routes.Auth.ME) }
            sessionManager.authorize()
        } catch (_: ApiException) {
            sessionManager.unauthorize()
        }
    }
}