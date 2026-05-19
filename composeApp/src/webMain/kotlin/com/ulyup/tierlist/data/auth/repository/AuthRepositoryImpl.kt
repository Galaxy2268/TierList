package com.ulyup.tierlist.data.auth.repository

import com.ulyup.tierlist.Routes
import com.ulyup.tierlist.core.network.session.SessionManager
import com.ulyup.tierlist.core.network.util.apiCall
import com.ulyup.tierlist.data.auth.mapper.toDomain
import com.ulyup.tierlist.domain.auth.model.User
import com.ulyup.tierlist.domain.auth.repository.AuthRepository
import com.ulyup.tierlist.dto.LoginRequest
import com.ulyup.tierlist.dto.RegisterRequest
import com.ulyup.tierlist.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionManager: SessionManager,
) : AuthRepository {

    override suspend fun login(usernameOrEmail: String, password: String): User = apiCall {
        val userDto = httpClient.post(Routes.Auth.LOGIN) {
            setBody(LoginRequest(usernameOrEmail = usernameOrEmail, password = password))
        }.body<UserDto>()
        sessionManager.authorize()
        userDto.toDomain()
    }

    override suspend fun register(username: String, email: String, password: String): User = apiCall {
        val userDto = httpClient.post(Routes.Auth.REGISTER) {
            setBody(RegisterRequest(username = username, email = email, password = password))
        }.body<UserDto>()
        sessionManager.authorize()
        userDto.toDomain()
    }
}