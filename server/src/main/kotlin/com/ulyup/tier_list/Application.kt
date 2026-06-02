package com.ulyup.tier_list

import com.ulyup.tier_list.auth.SessionKey
import com.ulyup.tier_list.auth.UserSession
import com.ulyup.tier_list.data.db.DatabaseFactory
import com.ulyup.tier_list.data.repository.FavouriteRepositoryImpl
import com.ulyup.tier_list.data.repository.ItemRepositoryImpl
import com.ulyup.tier_list.data.repository.TierListRepositoryImpl
import com.ulyup.tier_list.data.repository.UserRepositoryImpl
import com.ulyup.tier_list.data.storage.LocalImageStorage
import com.ulyup.tier_list.data.service.AuthServiceImpl
import com.ulyup.tier_list.domain.storage.ImageStorage
import com.ulyup.tier_list.data.service.ItemServiceImpl
import com.ulyup.tier_list.data.service.TierListServiceImpl
import com.ulyup.tier_list.data.service.UserServiceImpl
import com.ulyup.tier_list.dto.ErrorResponse
import com.ulyup.tier_list.routes.authRoutes
import com.ulyup.tier_list.routes.itemRoutes
import com.ulyup.tier_list.routes.tierListRoutes
import com.ulyup.tier_list.routes.userRoutes
import com.ulyup.tier_list.utils.BadRequestException
import com.ulyup.tier_list.utils.CapReachedException
import com.ulyup.tier_list.utils.ConflictException
import com.ulyup.tier_list.utils.ForbiddenException
import com.ulyup.tier_list.utils.NotFoundException
import com.ulyup.tier_list.utils.UnauthorizedException
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.json.Json
import java.io.File

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
            call.application.log.error("Unhandled exception", cause)
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Internal server error"))
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
    val tierListRepo = TierListRepositoryImpl()
    val itemRepo = ItemRepositoryImpl()
    val favouriteRepo = FavouriteRepositoryImpl()

    val imageStorage: ImageStorage = LocalImageStorage()
    File(LocalImageStorage.BASE_DIR).mkdirs()

    val authService = AuthServiceImpl(userRepo)
    val tierListService = TierListServiceImpl(tierListRepo, itemRepo, favouriteRepo, imageStorage)
    val itemService = ItemServiceImpl(itemRepo, tierListRepo, imageStorage)
    val userService = UserServiceImpl(userRepo)

    routing {
        staticFiles(LocalImageStorage.URL_PREFIX, File(LocalImageStorage.BASE_DIR))
        authRoutes(authService)
        tierListRoutes(tierListService)
        itemRoutes(itemService)
        userRoutes(userService)
    }
}
