package com.ulyup.tier_list.domain.storage

interface ImageStorage {
    suspend fun save(upload: ImageUpload): String
    suspend fun delete(url: String)
    suspend fun copy(sourceUrl: String): String
}
