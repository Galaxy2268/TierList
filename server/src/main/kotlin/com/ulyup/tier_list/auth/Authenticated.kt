package com.ulyup.tier_list.auth

import com.ulyup.tier_list.utils.UnauthorizedException
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

private val AuthPlugin = createRouteScopedPlugin("AuthPlugin") {
    onCall { call ->
        if (call.sessions.get<UserSession>() == null) {
            throw UnauthorizedException("Not authenticated")
        }
    }
}

fun Route.authenticated(block: Route.() -> Unit): Route {
    val authed = createChild(object : RouteSelector() {
        override suspend fun evaluate(context: RoutingResolveContext, segmentIndex: Int) =
            RouteSelectorEvaluation.Transparent
    })
    authed.install(AuthPlugin)
    authed.block()
    return authed
}