package com.ulyup.tierlist.auth

import java.io.File
import java.security.SecureRandom

object SessionKey {
    private const val PATH = "./data/session.key"
    private const val KEY_BYTES = 32

    fun loadOrCreate(): ByteArray {
        val file = File(PATH)
        if (file.exists()) return file.readBytes()
        file.parentFile?.mkdirs()
        val key = ByteArray(KEY_BYTES).also { SecureRandom().nextBytes(it) }
        file.writeBytes(key)
        return key
    }
}