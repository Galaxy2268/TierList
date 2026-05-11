package com.ulyup.tierlist.data.service

import com.ulyup.tierlist.auth.Passwords
import com.ulyup.tierlist.data.mapper.toDto
import com.ulyup.tierlist.domain.repository.UserRepository
import com.ulyup.tierlist.domain.service.AuthService
import com.ulyup.tierlist.dto.UserDto
import com.ulyup.tierlist.utils.ConflictException
import com.ulyup.tierlist.utils.UnauthorizedException

class AuthServiceImpl(private val userRepo: UserRepository) : AuthService {

    override suspend fun register(username: String, email: String, password: String): UserDto {
        if (password.length < 8) throw IllegalArgumentException("Password must be at least 8 characters")
        if (userRepo.findByUsername(username) != null || userRepo.findByEmail(email) != null) {
            throw ConflictException("Username or email already taken")
        }
        return userRepo.create(username, email, Passwords.hash(password)).toDto()
    }

    override suspend fun login(usernameOrEmail: String, password: String): UserDto {
        val user = userRepo.findByUsernameOrEmail(usernameOrEmail)
        if (user == null || !Passwords.verify(password, user.passwordHash)) {
            throw UnauthorizedException("Invalid credentials")
        }
        return user.toDto()
    }

    override suspend fun getUser(id: Int): UserDto {
        return userRepo.findById(id)?.toDto() ?: throw NoSuchElementException("User not found")
    }
}