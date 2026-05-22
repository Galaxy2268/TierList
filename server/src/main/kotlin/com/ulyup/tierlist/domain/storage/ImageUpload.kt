package com.ulyup.tierlist.domain.storage

import io.ktor.http.ContentType

class ImageUpload(
    val bytes: ByteArray,
    val contentType: ContentType,
)
