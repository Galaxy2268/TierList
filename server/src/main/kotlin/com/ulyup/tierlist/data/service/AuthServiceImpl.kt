package com.ulyup.tierlist.data.service

import com.ulyup.tierlist.auth.Passwords
import com.ulyup.tierlist.data.mapper.toDto
import com.ulyup.tierlist.domain.repository.UserRepository
import com.ulyup.tierlist.domain.service.AuthService
import com.ulyup.tierlist.dto.UserDto
import com.ulyup.tierlist.utils.BadRequestException
import com.ulyup.tierlist.utils.ConflictException
import com.ulyup.tierlist.utils.UnauthorizedException
import com.ulyup.tierlist.utils.findOrThrow
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException

class AuthServiceImpl(private val userRepo: UserRepository) : AuthService {

    override suspend fun register(username: String, email: String, password: String): UserDto {
        if (password.length < 8) throw BadRequestException("Password must be at least 8 characters")
        if (userRepo.findByUsername(username) != null || userRepo.findByEmail(email) != null) {
            throw ConflictException("Username or email already taken")
        }
        return try {
            userRepo.create(username, email, Passwords.hash(password)).toDto()
        } catch (_: ExposedSQLException) {
            throw ConflictException("Username or email already taken")
        }
    }

    override suspend fun login(usernameOrEmail: String, password: String): UserDto {
        val user = userRepo.findByUsernameOrEmail(usernameOrEmail)
        if (user == null || !Passwords.verify(password, user.passwordHash)) {
            throw UnauthorizedException("Invalid credentials")
        }
        return user.toDto()
    }

    override suspend fun getUser(id: Int): UserDto =
        findOrThrow("User") { userRepo.findById(id) }.toDto()
}