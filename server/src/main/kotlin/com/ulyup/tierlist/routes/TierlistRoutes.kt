package com.ulyup.tierlist.routes

import com.ulyup.tierlist.auth.UserSession
import com.ulyup.tierlist.auth.authenticated
import com.ulyup.tierlist.domain.model.Caller
import com.ulyup.tierlist.domain.service.TierlistService
import com.ulyup.tierlist.dto.CreateTierlistRequest
import com.ulyup.tierlist.dto.UpdateTierlistRequest
import com.ulyup.tierlist.dto.UpdateVisibilityRequest
import com.ulyup.tierlist.utils.caller
import com.ulyup.tierlist.utils.requireInt
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
            val id = call.parameters.requireInt("id")
            val caller = call.sessions.get<UserSession>()?.let { Caller(it.userId, it.role) }
            call.respond(tierlistService.getTierlist(caller, id))
        }

        authenticated {
            get("/users/me/tierlists") {
                call.respond(tierlistService.getUserTierlists(call.caller))
            }

            post("/tierlists") {
                val request = call.receive<CreateTierlistRequest>()
                call.respond(HttpStatusCode.Created, tierlistService.createTierlist(call.caller, request))
            }

            put("/tierlists/{id}") {
                val id = call.parameters.requireInt("id")
                val request = call.receive<UpdateTierlistRequest>()
                call.respond(tierlistService.updateTierlist(call.caller, id, request))
            }

            patch("/tierlists/{id}/visibility") {
                val id = call.parameters.requireInt("id")
                val request = call.receive<UpdateVisibilityRequest>()
                call.respond(tierlistService.setVisibility(call.caller, id, request))
            }

            delete("/tierlists/{id}") {
                val id = call.parameters.requireInt("id")
                tierlistService.deleteTierlist(call.caller, id)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}