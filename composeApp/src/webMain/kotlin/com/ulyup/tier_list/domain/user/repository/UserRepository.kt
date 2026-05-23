package com.ulyup.tier_list.domain.user.repository

import com.ulyup.tier_list.domain.auth.model.User

interface UserRepository {
    suspend fun me(): User
    suspend fun upgradePremium(): User
}
