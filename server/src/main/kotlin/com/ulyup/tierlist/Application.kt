package com.ulyup.tierlist

import com.ulyup.tierlist.auth.UserSession
import com.ulyup.tierlist.data.db.DatabaseFactory
import com.ulyup.tierlist.data.repository.ItemRepositoryImpl
import com.ulyup.tierlist.data.repository.TierlistRepositoryImpl
import com.ulyup.tierlist.data.repository.UserRepositoryImpl
import com.ulyup.tierlist.data.service.AuthServiceImpl
import com.ulyup.tierlist.data.service.TierlistServiceImpl
import com.ulyup.tierlist.routes.authRoutes
import com.ulyup.tierlist.routes.tierlistRoutes
import com.ulyup.tierlist.utils.ConflictException
import com.ulyup.tierlist.utils.UnauthorizedException
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(CallLogging)

    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respondText(cause.message ?: "Bad request", status = HttpStatusCode.BadRequest)
        }
        exception<NoSuchElementException> { call, cause ->
            call.respondText(cause.message ?: "Not found", status = HttpStatusCode.NotFound)
        }
        exception<SecurityException> { call, cause ->
            call.respondText(cause.message ?: "Forbidden", status = HttpStatusCode.Forbidden)
        }
        exception<IllegalStateException> { call, cause ->
            call.respondText(cause.message ?: "Forbidden", status = HttpStatusCode.Forbidden)
        }
        exception<ConflictException> { call, cause ->
            call.respondText(cause.message ?: "Conflict", status = HttpStatusCode.Conflict)
        }
        exception<UnauthorizedException> { call, cause ->
            call.respondText(cause.message ?: "Unauthorized", status = HttpStatusCode.Unauthorized)
        }
        exception<Throwable> { call, cause ->
            call.respondText(cause.message ?: "Internal server error", status = HttpStatusCode.InternalServerError)
        }
    }

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        })
    }

    install(Sessions) {
        cookie<UserSession>("TIERRANK_SESSION") {
            cookie.path = "/"
            cookie.httpOnly = true
            cookie.maxAgeInSeconds = 60L * 60 * 24 * 7
        }
    }

    install(CORS) {
        allowHost("localhost:8081", schemes = listOf("http"))
        allowCredentials = true
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.ContentType)
    }

    DatabaseFactory.init()

    val userRepo = UserRepositoryImpl()
    val tierlistRepo = TierlistRepositoryImpl()
    val itemRepo = ItemRepositoryImpl()

    val authService = AuthServiceImpl(userRepo)
    val tierlistService = TierlistServiceImpl(tierlistRepo, userRepo, itemRepo)

    routing {
        authRoutes(authService)
        tierlistRoutes(tierlistService)
    }
}