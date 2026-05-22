package com.ulyup.tierlist.data.service

import com.ulyup.tierlist.data.mapper.toDto
import com.ulyup.tierlist.domain.model.Caller
import com.ulyup.tierlist.domain.repository.UserRepository
import com.ulyup.tierlist.domain.service.UserService
import com.ulyup.tierlist.dto.UserDto
import com.ulyup.tierlist.model.UserRole
import com.ulyup.tierlist.utils.UnauthorizedException

class UserServiceImpl(private val userRepo: UserRepository) : UserService {

    override suspend fun getCurrentUser(caller: Caller): UserDto =
        userRepo.findById(caller.userId)?.toDto()
            ?: throw UnauthorizedException("Session no longer valid")

    override suspend fun upgradeToPremium(caller: Caller): UserDto =
        userRepo.setRole(caller.userId, UserRole.PREMIUM)?.toDto()
            ?: throw UnauthorizedException("Session no longer valid")
}
