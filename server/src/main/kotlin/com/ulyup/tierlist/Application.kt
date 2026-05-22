package com.ulyup.tierlist

import com.ulyup.tierlist.auth.SessionKey
import com.ulyup.tierlist.auth.UserSession
import com.ulyup.tierlist.data.db.DatabaseFactory
import com.ulyup.tierlist.data.repository.ItemRepositoryImpl
import com.ulyup.tierlist.data.repository.TierlistRepositoryImpl
import com.ulyup.tierlist.data.repository.UserRepositoryImpl
import com.ulyup.tierlist.data.storage.LocalImageStorage
import com.ulyup.tierlist.data.service.AuthServiceImpl
import com.ulyup.tierlist.domain.storage.ImageStorage
import com.ulyup.tierlist.data.service.ItemServiceImpl
import com.ulyup.tierlist.data.service.TierlistServiceImpl
import com.ulyup.tierlist.data.service.UserServiceImpl
import com.ulyup.tierlist.dto.ErrorResponse
import com.ulyup.tierlist.routes.authRoutes
import com.ulyup.tierlist.routes.itemRoutes
import com.ulyup.tierlist.routes.tierlistRoutes
import com.ulyup.tierlist.routes.userRoutes
import com.ulyup.tierlist.utils.BadRequestException
import com.ulyup.tierlist.utils.CapReachedException
import com.ulyup.tierlist.utils.ConflictException
import com.ulyup.tierlist.utils.ForbiddenException
import com.ulyup.tierlist.utils.NotFoundException
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
        exception<BadRequestException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(cause.message ?: "Bad request"))
        }
        exception<UnauthorizedException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized, ErrorResponse(cause.message ?: "Unauthorized"))
        }
        exception<ForbiddenException> { call, cause ->
            call.respond(HttpStatusCode.Forbidden, ErrorResponse(cause.message ?: "Forbidden"))
        }
        exception<CapReachedException> { call, cause ->
            call.respond(HttpStatusCode.Forbidden, ErrorResponse(cause.message ?: "Plan limit reached"))
        }
        exception<NotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, ErrorResponse(cause.message ?: "Not found"))
        }
        exception<ConflictException> { call, cause ->
            call.respond(HttpStatusCode.Conflict, ErrorResponse(cause.message ?: "Conflict"))
        }
        exception<Throwable> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse(cause.message ?: "Internal server error"))
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
            transform(SessionTransportTransformerMessageAuthentication(SessionKey.loadOrCreate()))
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

    val imageStorage: ImageStorage = LocalImageStorage()

    val authService = AuthServiceImpl(userRepo)
    val tierlistService = TierlistServiceImpl(tierlistRepo, itemRepo)
    val itemService = ItemServiceImpl(itemRepo, tierlistRepo, imageStorage)
    val userService = UserServiceImpl(userRepo)

    routing {
        authRoutes(authService)
        tierlistRoutes(tierlistService)
        itemRoutes(itemService)
        userRoutes(userService)
    }
}
