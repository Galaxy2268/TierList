package com.ulyup.tierlist.routes

import com.ulyup.tierlist.auth.UserSession
import com.ulyup.tierlist.auth.authenticated
import com.ulyup.tierlist.domain.service.UserService
import com.ulyup.tierlist.utils.userSession
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.userRoutes(userService: UserService) {
    route("/api/users/me") {
        authenticated {
            get {
                call.respond(userService.getCurrentUser(call.userSession))
            }

            patch("/upgrade-premium") {
                val updated = userService.upgradeToPremium(call.userSession)
                call.sessions.set(UserSession(updated.id, updated.username, updated.role))
                call.respond(updated)
            }
        }
    }
}