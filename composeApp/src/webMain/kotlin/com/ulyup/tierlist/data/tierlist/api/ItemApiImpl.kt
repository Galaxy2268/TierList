package com.ulyup.tierlist.data.tierlist.api

import com.ulyup.tierlist.Routes
import com.ulyup.tierlist.data.network.util.apiCall
import com.ulyup.tierlist.dto.CreateItemRequest
import com.ulyup.tierlist.dto.ItemDto
import com.ulyup.tierlist.dto.MoveItemRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class ItemApiImpl(private val httpClient: HttpClient) : ItemApi {

    override suspend fun create(tierlistId: Int, request: CreateItemRequest): ItemDto = apiCall {
        httpClient.post(Routes.Items.root(tierlistId)) { setBody(request) }.body()
    }

    override suspend fun delete(tierlistId: Int, itemId: Int) {
        apiCall { httpClient.delete(Routes.Items.byId(tierlistId, itemId)) }
    }

    override suspend fun move(tierlistId: Int, itemId: Int, request: MoveItemRequest): ItemDto = apiCall {
        httpClient.patch(Routes.Items.move(tierlistId, itemId)) { setBody(request) }.body()
    }
}
