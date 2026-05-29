package com.ulyup.tier_list.routes

import com.ulyup.tier_list.Routes
import com.ulyup.tier_list.auth.UserSession
import com.ulyup.tier_list.auth.authenticated
import com.ulyup.tier_list.domain.model.Caller
import com.ulyup.tier_list.domain.service.ItemService
import com.ulyup.tier_list.dto.MoveItemRequest
import com.ulyup.tier_list.dto.UpdateItemRequest
import com.ulyup.tier_list.utils.caller
import com.ulyup.tier_list.utils.receiveImageUploads
import com.ulyup.tier_list.utils.requireInt
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.itemRoutes(itemService: ItemService) {
    get(Routes.Items.ROOT) {
        val tierListId = call.parameters.requireInt(Routes.Items.TIER_LIST_ID_PARAM)
        val caller = call.sessions.get<UserSession>()?.let { Caller(it.userId, it.role) }
        call.respond(itemService.getItems(caller, tierListId))
    }

    authenticated {
        post(Routes.Items.BATCH) {
            val tierListId = call.parameters.requireInt(Routes.Items.TIER_LIST_ID_PARAM)
            val images = call.receiveImageUploads()
            call.respond(HttpStatusCode.Created, itemService.createItems(call.caller, tierListId, images))
        }

        put(Routes.Items.BY_ID) {
            val tierListId = call.parameters.requireInt(Routes.Items.TIER_LIST_ID_PARAM)
            val itemId = call.parameters.requireInt(Routes.Items.ITEM_ID_PARAM)
            val request = call.receive<UpdateItemRequest>()
            call.respond(itemService.updateItem(call.caller, tierListId, itemId, request))
        }

        patch(Routes.Items.MOVE) {
            val tierListId = call.parameters.requireInt(Routes.Items.TIER_LIST_ID_PARAM)
            val itemId = call.parameters.requireInt(Routes.Items.ITEM_ID_PARAM)
            val request = call.receive<MoveItemRequest>()
            call.respond(itemService.moveItem(call.caller, tierListId, itemId, request))
        }

        delete(Routes.Items.BY_ID) {
            val tierListId = call.parameters.requireInt(Routes.Items.TIER_LIST_ID_PARAM)
            val itemId = call.parameters.requireInt(Routes.Items.ITEM_ID_PARAM)
            itemService.deleteItem(call.caller, tierListId, itemId)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
