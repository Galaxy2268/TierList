package com.ulyup.tierlist.domain.storage

interface ImageStorage {
    suspend fun save(upload: ImageUpload): String
    suspend fun delete(url: String)
}
