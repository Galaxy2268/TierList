package com.ulyup.tierlist.utils

import com.ulyup.tierlist.auth.UserSession
import com.ulyup.tierlist.domain.model.Tierlist
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.sessions.*


fun UserSession.requireOwnership(tierlist: Tierlist) {
    if (userId != tierlist.userId) throw SecurityException("You do not own this tierlist")
}

suspend fun <T> findOrThrow(name: String, find: suspend () -> T?): T =
    find() ?: throw NoSuchElementException("$name not found")

val ApplicationCall.userSession: UserSession
    get() = sessions.get<UserSession>()!!

fun Parameters.requireInt(name: String): Int =
    get(name)?.toIntOrNull() ?: throw IllegalArgumentException("Invalid $name")