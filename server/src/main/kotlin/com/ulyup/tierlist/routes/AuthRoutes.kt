package com.ulyup.tierlist.routes

import com.ulyup.tierlist.auth.Passwords
import com.ulyup.tierlist.auth.UserSession
import com.ulyup.tierlist.auth.authenticated
import com.ulyup.tierlist.domain.repository.UserRepository
import com.ulyup.tierlist.dto.LoginRequest
import com.ulyup.tierlist.dto.RegisterRequest
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.authRoutes(userRepo: UserRepository) {
    route("/api/auth") {
        post("/register") {
            val req = call.receive<RegisterRequest>()
            if (req.password.length < 8) {
                throw IllegalArgumentException("Password must be at least 8 characters")
            }
            if (userRepo.findByUsername(req.username) != null || userRepo.findByEmail(req.email) != null) {
                call.respond(HttpStatusCode.Conflict, "Username or email already taken")
                return@post
            }
            val user = userRepo.create(req.username, req.email, Passwords.hash(req.password))
            call.sessions.set(UserSession(user.id, user.username, user.role))
            call.respond(HttpStatusCode.Created, user.toDto())
        }

        post("/login") {
            val req = call.receive<LoginRequest>()
            val user = userRepo.findByUsernameOrEmail(req.usernameOrEmail)
            if (user == null || !Passwords.verify(req.password, user.passwordHash)) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                return@post
            }
            call.sessions.set(UserSession(user.id, user.username, user.role))
            call.respond(user.toDto())
        }

        authenticated {
            post("/logout") {
                call.sessions.clear<UserSession>()
                call.respond(HttpStatusCode.NoContent)
            }

            get("/me") {
                val session = call.sessions.get<UserSession>()!!
                val user = userRepo.findById(session.userId)
                    ?: throw NoSuchElementException("User not found")
                call.respond(user.toDto())
            }
        }
    }
}