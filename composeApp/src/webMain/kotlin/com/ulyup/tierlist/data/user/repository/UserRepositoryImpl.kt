package com.ulyup.tierlist.data.user.repository

import com.ulyup.tierlist.data.auth.mapper.toDomain
import com.ulyup.tierlist.data.user.api.UserApi
import com.ulyup.tierlist.domain.auth.model.User
import com.ulyup.tierlist.domain.user.repository.UserRepository

class UserRepositoryImpl(
    private val userApi: UserApi,
) : UserRepository {

    override suspend fun me(): User = userApi.me().toDomain()

    override suspend fun upgradePremium(): User = userApi.upgradePremium().toDomain()
}
