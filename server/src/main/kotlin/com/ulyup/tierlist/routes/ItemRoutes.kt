package com.ulyup.tierlist.routes

import com.ulyup.tierlist.auth.UserSession
import com.ulyup.tierlist.auth.authenticated
import com.ulyup.tierlist.domain.service.ItemService
import com.ulyup.tierlist.dto.CreateItemRequest
import com.ulyup.tierlist.dto.UpdateItemRequest
import com.ulyup.tierlist.utils.requireInt
import com.ulyup.tierlist.utils.userSession
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.itemRoutes(itemService: ItemService) {
    route("/api/tierlists/{id}/items") {
        get {
            val tierlistId = call.parameters.requireInt("id")
            val session = call.sessions.get<UserSession>()
            call.respond(itemService.getItems(session, tierlistId))
        }

        authenticated {
            post {
                val tierlistId = call.parameters.requireInt("id")
                val request = call.receive<CreateItemRequest>()
                call.respond(HttpStatusCode.Created, itemService.createItem(call.userSession, tierlistId, request))
            }

            put("/{itemId}") {
                val tierlistId = call.parameters.requireInt("id")
                val itemId = call.parameters.requireInt("itemId")
                val request = call.receive<UpdateItemRequest>()
                call.respond(itemService.updateItem(call.userSession, tierlistId, itemId, request))
            }

            delete("/{itemId}") {
                val tierlistId = call.parameters.requireInt("id")
                val itemId = call.parameters.requireInt("itemId")
                itemService.deleteItem(call.userSession, tierlistId, itemId)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
