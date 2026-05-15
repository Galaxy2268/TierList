package com.ulyup.tierlist.routes

import com.ulyup.tierlist.auth.UserSession
import com.ulyup.tierlist.auth.authenticated
import com.ulyup.tierlist.domain.service.UserService
import com.ulyup.tierlist.utils.caller
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.userRoutes(userService: UserService) {
    route("/api/users/me") {
        authenticated {
            get {
                call.respond(userService.getCurrentUser(call.caller))
            }

            patch("/upgrade-premium") {
                val updated = userService.upgradeToPremium(call.caller)
                call.sessions.set(UserSession(updated.id, updated.username, updated.role))
                call.respond(updated)
            }
        }
    }
}