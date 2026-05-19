package com.ulyup.tierlist.data.user.api

import com.ulyup.tierlist.Routes
import com.ulyup.tierlist.data.network.util.apiCall
import com.ulyup.tierlist.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch

class UserApiImpl(private val httpClient: HttpClient) : UserApi {

    override suspend fun me(): UserDto = apiCall {
        httpClient.get(Routes.Users.ME).body()
    }

    override suspend fun upgradePremium(): UserDto = apiCall {
        httpClient.patch(Routes.Users.UPGRADE_PREMIUM).body()
    }
}
