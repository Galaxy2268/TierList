package com.ulyup.tier_list.data.service

import com.ulyup.tier_list.data.mapper.toDto
import com.ulyup.tier_list.domain.model.Caller
import com.ulyup.tier_list.domain.repository.UserRepository
import com.ulyup.tier_list.domain.service.UserService
import com.ulyup.tier_list.dto.UserDto
import com.ulyup.tier_list.model.UserRole
import com.ulyup.tier_list.utils.UnauthorizedException

class UserServiceImpl(private val userRepo: UserRepository) : UserService {

    override suspend fun getCurrentUser(caller: Caller): UserDto =
        userRepo.findById(caller.userId)?.toDto()
            ?: throw UnauthorizedException("Session no longer valid")

    override suspend fun upgradeToPremium(caller: Caller): UserDto =
        userRepo.setRole(caller.userId, UserRole.PREMIUM)?.toDto()
            ?: throw UnauthorizedException("Session no longer valid")
}
