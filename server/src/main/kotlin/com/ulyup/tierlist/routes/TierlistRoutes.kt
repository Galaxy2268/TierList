package com.ulyup.tierlist.routes

import com.ulyup.tierlist.auth.UserSession
import com.ulyup.tierlist.auth.authenticated
import com.ulyup.tierlist.domain.service.TierlistService
import com.ulyup.tierlist.dto.CreateTierlistRequest
import com.ulyup.tierlist.dto.UpdateTierlistRequest
import com.ulyup.tierlist.dto.UpdateVisibilityRequest
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.tierlistRoutes(tierlistService: TierlistService) {
    route("/api") {
        get("/tierlists") {
            call.respond(tierlistService.getPublicFeed())
        }

        get("/tierlists/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: throw IllegalArgumentException("Invalid id")
            val session = call.sessions.get<UserSession>()
            call.respond(tierlistService.getTierlist(session, id))
        }

        authenticated {
            get("/users/me/tierlists") {
                val session = call.sessions.get<UserSession>()!!
                call.respond(tierlistService.getUserTierlists(session))
            }

            post("/tierlists") {
                val session = call.sessions.get<UserSession>()!!
                val request = call.receive<CreateTierlistRequest>()
                call.respond(HttpStatusCode.Created, tierlistService.createTierlist(session, request))
            }

            put("/tierlists/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: throw IllegalArgumentException("Invalid id")
                val session = call.sessions.get<UserSession>()!!
                val request = call.receive<UpdateTierlistRequest>()
                call.respond(tierlistService.updateTierlist(session, id, request))
            }

            patch("/tierlists/{id}/visibility") {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: throw IllegalArgumentException("Invalid id")
                val session = call.sessions.get<UserSession>()!!
                val request = call.receive<UpdateVisibilityRequest>()
                call.respond(tierlistService.setVisibility(session, id, request))
            }

            delete("/tierlists/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: throw IllegalArgumentException("Invalid id")
                val session = call.sessions.get<UserSession>()!!
                tierlistService.deleteTierlist(session, id)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}