package com.ulyup.tierlist.data.user.repository

import com.ulyup.tierlist.data.auth.mapper.toDomain
import com.ulyup.tierlist.data.session.SessionManager
import com.ulyup.tierlist.data.user.api.UserApi
import com.ulyup.tierlist.domain.auth.model.User
import com.ulyup.tierlist.domain.user.repository.UserRepository

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val sessionManager: SessionManager,
) : UserRepository {

    override suspend fun me(): User {
        val user = userApi.me().toDomain()
        sessionManager.setCurrentUser(user)
        return user
    }

    override suspend fun upgradePremium(): User {
        val user = userApi.upgradePremium().toDomain()
        sessionManager.setCurrentUser(user)
        return user
    }
}
