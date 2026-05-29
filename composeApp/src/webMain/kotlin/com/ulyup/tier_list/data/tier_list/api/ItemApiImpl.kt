package com.ulyup.tier_list.data.tier_list.api

import com.ulyup.tier_list.MULTIPART_IMAGE_PART
import com.ulyup.tier_list.Routes
import com.ulyup.tier_list.data.network.util.apiCall
import com.ulyup.tier_list.domain.tier_list.model.ItemImage
import com.ulyup.tier_list.dto.CreateItemsBatchResponse
import com.ulyup.tier_list.dto.ItemDto
import com.ulyup.tier_list.dto.MoveItemRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class ItemApiImpl(private val httpClient: HttpClient) : ItemApi {

    override suspend fun createBatch(tierListId: Int, images: List<ItemImage>): CreateItemsBatchResponse = apiCall {
        val body = MultiPartFormDataContent(
            formData {
                images.forEach { image ->
                    append(
                        key = MULTIPART_IMAGE_PART,
                        value = image.bytes,
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, filenameToContentType(image.filename))
                            append(HttpHeaders.ContentDisposition, contentDispositionFilename(image.filename))
                        },
                    )
                }
            }
        )
        httpClient.post(Routes.Items.batch(tierListId)) { setBody(body) }.body()
    }

    override suspend fun delete(tierListId: Int, itemId: Int) {
        apiCall { httpClient.delete(Routes.Items.byId(tierListId, itemId)) }
    }

    override suspend fun clear(tierListId: Int) {
        apiCall { httpClient.delete(Routes.Items.root(tierListId)) }
    }

    override suspend fun move(tierListId: Int, itemId: Int, request: MoveItemRequest): ItemDto = apiCall {
        httpClient.patch(Routes.Items.move(tierListId, itemId)) { setBody(request) }.body()
    }

    // RFC 6266 quoted-string: escape backslash and quote so a filename like a"b.png can't break the header.
    private fun contentDispositionFilename(filename: String): String {
        val escaped = filename.replace("\\", "\\\\").replace("\"", "\\\"")
        return "filename=\"$escaped\""
    }

    // Inverse of LocalImageStorage.contentTypeToExtension on the server — keep tables in sync.
    private fun filenameToContentType(filename: String): String =
        when (filename.substringAfterLast('.', "").lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "webp" -> "image/webp"
            else -> "application/octet-stream"
        }
}
