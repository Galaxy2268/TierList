package com.ulyup.tierlist.routes

import com.ulyup.tierlist.auth.UserSession
import com.ulyup.tierlist.auth.authenticated
import com.ulyup.tierlist.domain.model.Caller
import com.ulyup.tierlist.domain.service.ItemService
import com.ulyup.tierlist.dto.CreateItemRequest
import com.ulyup.tierlist.dto.MoveItemRequest
import com.ulyup.tierlist.dto.UpdateItemRequest
import com.ulyup.tierlist.utils.caller
import com.ulyup.tierlist.utils.requireInt
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.itemRoutes(itemService: ItemService) {
    route("/api/tierlists/{id}/items") {
        get {
            val tierlistId = call.parameters.requireInt("id")
            val caller = call.sessions.get<UserSession>()?.let { Caller(it.userId, it.role) }
            call.respond(itemService.getItems(caller, tierlistId))
        }

        authenticated {
            post {
                val tierlistId = call.parameters.requireInt("id")
                val request = call.receive<CreateItemRequest>()
                call.respond(HttpStatusCode.Created, itemService.createItem(call.caller, tierlistId, request))
            }

            put("/{itemId}") {
                val tierlistId = call.parameters.requireInt("id")
                val itemId = call.parameters.requireInt("itemId")
                val request = call.receive<UpdateItemRequest>()
                call.respond(itemService.updateItem(call.caller, tierlistId, itemId, request))
            }

            patch("/{itemId}/move") {
                val tierlistId = call.parameters.requireInt("id")
                val itemId = call.parameters.requireInt("itemId")
                val request = call.receive<MoveItemRequest>()
                call.respond(itemService.moveItem(call.caller, tierlistId, itemId, request))
            }

            delete("/{itemId}") {
                val tierlistId = call.parameters.requireInt("id")
                val itemId = call.parameters.requireInt("itemId")
                itemService.deleteItem(call.caller, tierlistId, itemId)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
