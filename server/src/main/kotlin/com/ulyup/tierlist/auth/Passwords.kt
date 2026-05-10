package com.ulyup.tierlist.auth

import at.favre.lib.crypto.bcrypt.BCrypt

object Passwords {
    private val hasher = BCrypt.withDefaults()
    private val verifier = BCrypt.verifyer()

    fun hash(raw: String): String = hasher.hashToString(12, raw.toCharArray())

    fun verify(raw: String, hash: String): Boolean =
        verifier.verify(raw.toCharArray(), hash).verified
}