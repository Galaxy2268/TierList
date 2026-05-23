package com.ulyup.tier_list.utils

import com.ulyup.tier_list.auth.UserSession
import com.ulyup.tier_list.domain.model.Caller
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.sessions.*

suspend fun <T> findOrThrow(name: String, find: suspend () -> T?): T =
    find() ?: throw NotFoundException("$name not found")

val ApplicationCall.caller: Caller
    get() = sessions.get<UserSession>()?.let { Caller(it.userId, it.role) }
        ?: throw UnauthorizedException("Not authenticated")

fun Parameters.requireInt(name: String): Int =
    get(name)?.toIntOrNull() ?: throw BadRequestException("Invalid $name")
