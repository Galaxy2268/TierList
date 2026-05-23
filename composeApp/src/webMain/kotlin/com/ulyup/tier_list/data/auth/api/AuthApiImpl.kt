package com.ulyup.tier_list.data.auth.api

import com.ulyup.tier_list.Routes
import com.ulyup.tier_list.data.network.util.apiCall
import com.ulyup.tier_list.dto.LoginRequest
import com.ulyup.tier_list.dto.RegisterRequest
import com.ulyup.tier_list.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
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
}
