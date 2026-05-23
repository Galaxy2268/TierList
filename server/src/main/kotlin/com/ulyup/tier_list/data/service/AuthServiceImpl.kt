package com.ulyup.tier_list.data.service

import com.ulyup.tier_list.auth.Passwords
import com.ulyup.tier_list.data.mapper.toDto
import com.ulyup.tier_list.domain.repository.UserRepository
import com.ulyup.tier_list.domain.service.AuthService
import com.ulyup.tier_list.dto.UserDto
import com.ulyup.tier_list.utils.BadRequestException
import com.ulyup.tier_list.utils.UnauthorizedException

private val usernamePattern = Regex("^[a-zA-Z0-9_]{3,32}$")
private val emailPattern = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")

class AuthServiceImpl(private val userRepo: UserRepository) : AuthService {

    override suspend fun register(username: String, email: String, password: String): UserDto {
        if (!usernamePattern.matches(username)) {
            throw BadRequestException("Username must be 3-32 characters: letters, digits, underscore")
        }
        if (email.length > 255 || !emailPattern.matches(email)) {
            throw BadRequestException("Email must be a valid address up to 255 characters")
        }
        if (password.length !in 8..128) {
            throw BadRequestException("Password must be 8-128 characters")
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
}
