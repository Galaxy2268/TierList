package com.ulyup.tierlist.domain.user.repository

import com.ulyup.tierlist.domain.auth.model.User

interface UserRepository {
    suspend fun me(): User
    suspend fun upgradePremium(): User
}
