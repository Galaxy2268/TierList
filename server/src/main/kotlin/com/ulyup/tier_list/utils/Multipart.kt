package com.ulyup.tier_list.utils

import com.ulyup.tier_list.MAX_BATCH_IMAGES
import com.ulyup.tier_list.MULTIPART_IMAGE_PART
import com.ulyup.tier_list.domain.storage.IncomingImage
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receiveMultipart
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray

const val MAX_IMAGE_BYTES = 5L * 1024 * 1024

suspend fun ApplicationCall.receiveImageUploads(maxBytes: Long = MAX_IMAGE_BYTES): List<IncomingImage> {
    val images = mutableListOf<IncomingImage>()
    receiveMultipart().forEachPart { part ->
        if (part is PartData.FileItem && part.name == MULTIPART_IMAGE_PART) {
            // Cap while reading, not after — otherwise an oversized batch buffers fully before rejection.
            if (images.size >= MAX_BATCH_IMAGES) {
                part.dispose()
                throw BadRequestException("Too many images: max $MAX_BATCH_IMAGES per upload")
            }
            images += IncomingImage(
                filename = part.originalFileName ?: "image",
                bytes = part.provider().readRemaining(maxBytes + 1).readByteArray(),
                contentType = part.contentType,
            )
        }
        part.dispose()
    }
    if (images.isEmpty()) throw BadRequestException("Missing image part")
    return images
}
