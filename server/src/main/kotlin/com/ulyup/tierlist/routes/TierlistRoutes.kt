package com.ulyup.tierlist.routes

import com.ulyup.tierlist.Routes
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
    get(Routes.Tierlists.ROOT) {
        call.respond(tierlistService.getPublicFeed())
    }

    get(Routes.Tierlists.BY_ID) {
        val tierlistId = call.parameters.requireInt(Routes.Tierlists.ID_PARAM)
        val caller = call.sessions.get<UserSession>()?.let { Caller(it.userId, it.role) }
        call.respond(tierlistService.getTierlist(caller, tierlistId))
    }

    authenticated {
        get(Routes.Tierlists.MINE) {
            call.respond(tierlistService.getUserTierlists(call.caller))
        }

        post(Routes.Tierlists.ROOT) {
            val request = call.receive<CreateTierlistRequest>()
            call.respond(HttpStatusCode.Created, tierlistService.createTierlist(call.caller, request))
        }

        put(Routes.Tierlists.BY_ID) {
            val tierlistId = call.parameters.requireInt(Routes.Tierlists.ID_PARAM)
            val request = call.receive<UpdateTierlistRequest>()
            call.respond(tierlistService.updateTierlist(call.caller, tierlistId, request))
        }

        patch(Routes.Tierlists.VISIBILITY) {
            val tierlistId = call.parameters.requireInt(Routes.Tierlists.ID_PARAM)
            val request = call.receive<UpdateVisibilityRequest>()
            call.respond(tierlistService.setVisibility(call.caller, tierlistId, request))
        }

        delete(Routes.Tierlists.BY_ID) {
            val tierlistId = call.parameters.requireInt(Routes.Tierlists.ID_PARAM)
            tierlistService.deleteTierlist(call.caller, tierlistId)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
