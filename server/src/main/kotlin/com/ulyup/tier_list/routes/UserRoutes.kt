package com.ulyup.tier_list.routes

import com.ulyup.tier_list.Routes
import com.ulyup.tier_list.auth.UserSession
import com.ulyup.tier_list.auth.authenticated
import com.ulyup.tier_list.domain.service.UserService
import com.ulyup.tier_list.utils.caller
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.userRoutes(userService: UserService) {
    authenticated {
        get(Routes.Users.ME) {
            call.respond(userService.getCurrentUser(call.caller))
        }

        patch(Routes.Users.UPGRADE_PREMIUM) {
            val updated = userService.upgradeToPremium(call.caller)
            call.sessions.set(UserSession(updated.id, updated.username, updated.role))
            call.respond(updated)
        }
    }
}
