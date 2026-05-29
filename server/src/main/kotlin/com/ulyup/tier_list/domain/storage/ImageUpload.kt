package com.ulyup.tier_list.domain.storage

import io.ktor.http.ContentType

class ImageUpload(
    val bytes: ByteArray,
    val contentType: ContentType,
)

class IncomingImage(
    val filename: String,
    val bytes: ByteArray,
    val contentType: ContentType?,
)
