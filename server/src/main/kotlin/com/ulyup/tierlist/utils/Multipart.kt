package com.ulyup.tierlist.utils

import com.ulyup.tierlist.MULTIPART_IMAGE_PART
import com.ulyup.tierlist.domain.storage.ImageUpload
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receiveMultipart
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray

const val MAX_IMAGE_BYTES = 5L * 1024 * 1024

suspend fun ApplicationCall.receiveImageUpload(maxBytes: Long = MAX_IMAGE_BYTES): ImageUpload {
    var upload: ImageUpload? = null
    receiveMultipart().forEachPart { part ->
        if (upload == null && part is PartData.FileItem && part.name == MULTIPART_IMAGE_PART) {
            val contentType = part.contentType
                ?: throw BadRequestException("Missing image content type")
            upload = ImageUpload(part.readBytesCapped(maxBytes), contentType)
        }
        part.dispose()
    }
    return upload ?: throw BadRequestException("Missing image part")
}

private suspend fun PartData.FileItem.readBytesCapped(maxBytes: Long): ByteArray {
    val bytes = provider().readRemaining(maxBytes + 1).readByteArray()
    if (bytes.size > maxBytes) throw BadRequestException("Image exceeds size limit")
    return bytes
}
