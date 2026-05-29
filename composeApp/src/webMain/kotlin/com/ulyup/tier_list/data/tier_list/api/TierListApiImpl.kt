package com.ulyup.tier_list.data.tier_list.api

import com.ulyup.tier_list.Routes
import com.ulyup.tier_list.data.network.util.apiCall
import com.ulyup.tier_list.dto.CreateTierListRequest
import com.ulyup.tier_list.dto.TierListDetailDto
import com.ulyup.tier_list.dto.TierListDto
import com.ulyup.tier_list.dto.UpdateTierListRequest
import com.ulyup.tier_list.dto.UpdateVisibilityRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class TierListApiImpl(private val httpClient: HttpClient) : TierListApi {

    override suspend fun getPublicTierLists(): List<TierListDto> = apiCall {
        httpClient.get(Routes.TierLists.ROOT).body()
    }

    override suspend fun getUserTierLists(): List<TierListDto> = apiCall {
        httpClient.get(Routes.TierLists.MINE).body()
    }

    override suspend fun create(request: CreateTierListRequest): TierListDto = apiCall {
        httpClient.post(Routes.TierLists.ROOT) { setBody(request) }.body()
    }

    override suspend fun getDetail(id: Int): TierListDetailDto = apiCall {
        httpClient.get(Routes.TierLists.detail(id)).body()
    }

    override suspend fun update(id: Int, request: UpdateTierListRequest): TierListDto = apiCall {
        httpClient.put(Routes.TierLists.detail(id)) { setBody(request) }.body()
    }

    override suspend fun setVisibility(id: Int, request: UpdateVisibilityRequest): TierListDto = apiCall {
        httpClient.patch(Routes.TierLists.visibility(id)) { setBody(request) }.body()
    }

    override suspend fun setFavourite(id: Int, favourite: Boolean) {
        apiCall {
            if (favourite) httpClient.put(Routes.TierLists.favourite(id))
            else httpClient.delete(Routes.TierLists.favourite(id))
        }
    }

    override suspend fun delete(id: Int) {
        apiCall { httpClient.delete(Routes.TierLists.detail(id)) }
    }
}
