package com.ulyup.tierlist.data.tierlist.api

import com.ulyup.tierlist.Routes
import com.ulyup.tierlist.data.network.util.apiCall
import com.ulyup.tierlist.dto.CreateTierlistRequest
import com.ulyup.tierlist.dto.TierlistDetailDto
import com.ulyup.tierlist.dto.TierlistDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class TierlistApiImpl(private val httpClient: HttpClient) : TierlistApi {

    override suspend fun getPublicTierlists(): List<TierlistDto> = apiCall {
        httpClient.get(Routes.Tierlists.ROOT).body()
    }

    override suspend fun getUserTierlists(): List<TierlistDto> = apiCall {
        httpClient.get(Routes.Tierlists.MINE).body()
    }

    override suspend fun create(request: CreateTierlistRequest): TierlistDto = apiCall {
        httpClient.post(Routes.Tierlists.ROOT) { setBody(request) }.body()
    }

    override suspend fun getDetail(id: Int): TierlistDetailDto = apiCall {
        httpClient.get(Routes.Tierlists.detail(id)).body()
    }
}
