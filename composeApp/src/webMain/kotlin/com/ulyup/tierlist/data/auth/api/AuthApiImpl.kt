package com.ulyup.tierlist.data.auth.api

import com.ulyup.tierlist.Routes
import com.ulyup.tierlist.data.network.util.apiCall
import com.ulyup.tierlist.dto.LoginRequest
import com.ulyup.tierlist.dto.RegisterRequest
import com.ulyup.tierlist.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthApiImpl(private val httpClient: HttpClient) : AuthApi {

    override suspend fun login(request: LoginRequest): UserDto = apiCall {
        httpClient.post(Routes.Auth.LOGIN) { setBody(request) }.body()
    }

    override suspend fun register(request: RegisterRequest): UserDto = apiCall {
        httpClient.post(Routes.Auth.REGISTER) { setBody(request) }.body()
    }

    override suspend fun logout() {
        apiCall { httpClient.post(Routes.Auth.LOGOUT) }
    }

    override suspend fun me(): UserDto = apiCall {
        httpClient.get(Routes.Auth.ME).body()
    }
}