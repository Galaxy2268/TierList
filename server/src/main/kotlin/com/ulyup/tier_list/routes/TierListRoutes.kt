package com.ulyup.tier_list.routes

import com.ulyup.tier_list.Routes
import com.ulyup.tier_list.auth.UserSession
import com.ulyup.tier_list.auth.authenticated
import com.ulyup.tier_list.domain.model.Caller
import com.ulyup.tier_list.domain.service.TierListService
import com.ulyup.tier_list.dto.CreateTierListRequest
import com.ulyup.tier_list.dto.UpdateTierListRequest
import com.ulyup.tier_list.dto.UpdateVisibilityRequest
import com.ulyup.tier_list.utils.caller
import com.ulyup.tier_list.utils.requireInt
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.tierListRoutes(tierListService: TierListService) {
    get(Routes.TierLists.ROOT) {
        val caller = call.sessions.get<UserSession>()?.let { Caller(it.userId, it.role) }
        call.respond(tierListService.getPublicFeed(caller))
    }

    get(Routes.TierLists.BY_ID) {
        val tierListId = call.parameters.requireInt(Routes.TierLists.ID_PARAM)
        val caller = call.sessions.get<UserSession>()?.let { Caller(it.userId, it.role) }
        call.respond(tierListService.getTierList(caller, tierListId))
    }

    authenticated {
        get(Routes.TierLists.MINE) {
            call.respond(tierListService.getUserTierLists(call.caller))
        }

        post(Routes.TierLists.ROOT) {
            val request = call.receive<CreateTierListRequest>()
            call.respond(HttpStatusCode.Created, tierListService.createTierList(call.caller, request))
        }

        post(Routes.TierLists.COPY) {
            val tierListId = call.parameters.requireInt(Routes.TierLists.ID_PARAM)
            call.respond(HttpStatusCode.Created, tierListService.copyTierList(call.caller, tierListId))
        }

        put(Routes.TierLists.BY_ID) {
            val tierListId = call.parameters.requireInt(Routes.TierLists.ID_PARAM)
            val request = call.receive<UpdateTierListRequest>()
            call.respond(tierListService.updateTierList(call.caller, tierListId, request))
        }

        patch(Routes.TierLists.VISIBILITY) {
            val tierListId = call.parameters.requireInt(Routes.TierLists.ID_PARAM)
            val request = call.receive<UpdateVisibilityRequest>()
            call.respond(tierListService.setVisibility(call.caller, tierListId, request))
        }

        delete(Routes.TierLists.BY_ID) {
            val tierListId = call.parameters.requireInt(Routes.TierLists.ID_PARAM)
            tierListService.deleteTierList(call.caller, tierListId)
            call.respond(HttpStatusCode.NoContent)
        }

        put(Routes.TierLists.FAVOURITE) {
            val tierListId = call.parameters.requireInt(Routes.TierLists.ID_PARAM)
            tierListService.setFavourite(call.caller, tierListId, favourite = true)
            call.respond(HttpStatusCode.NoContent)
        }

        delete(Routes.TierLists.FAVOURITE) {
            val tierListId = call.parameters.requireInt(Routes.TierLists.ID_PARAM)
            tierListService.setFavourite(call.caller, tierListId, favourite = false)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
