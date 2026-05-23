package com.ulyup.tier_list.routes

import com.ulyup.tier_list.Routes
import com.ulyup.tier_list.auth.UserSession
import com.ulyup.tier_list.auth.authenticated
import com.ulyup.tier_list.domain.service.AuthService
import com.ulyup.tier_list.dto.LoginRequest
import com.ulyup.tier_list.dto.RegisterRequest
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.authRoutes(authService: AuthService) {
    post(Routes.Auth.REGISTER) {
        val request = call.receive<RegisterRequest>()
        val user = authService.register(request.username, request.email, request.password)
        call.sessions.set(UserSession(user.id, user.username, user.role))
        call.respond(HttpStatusCode.Created, user)
    }

    post(Routes.Auth.LOGIN) {
        val request = call.receive<LoginRequest>()
        val user = authService.login(request.usernameOrEmail, request.password)
        call.sessions.set(UserSession(user.id, user.username, user.role))
        call.respond(user)
    }

    authenticated {
        post(Routes.Auth.LOGOUT) {
            call.sessions.clear<UserSession>()
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
