package com.ulyup.tierlist.data.service

import com.ulyup.tierlist.data.mapper.toDto
import com.ulyup.tierlist.domain.model.Caller
import com.ulyup.tierlist.domain.repository.UserRepository
import com.ulyup.tierlist.domain.service.UserService
import com.ulyup.tierlist.dto.UserDto
import com.ulyup.tierlist.model.UserRole
import com.ulyup.tierlist.utils.findOrThrow

class UserServiceImpl(private val userRepo: UserRepository) : UserService {

    override suspend fun getCurrentUser(caller: Caller): UserDto =
        findOrThrow("User") { userRepo.findById(caller.userId) }.toDto()

    override suspend fun upgradeToPremium(caller: Caller): UserDto =
        findOrThrow("User") { userRepo.setRole(caller.userId, UserRole.PREMIUM) }.toDto()
}