package com.ulyup.tierlist.data.tierlist.api

import com.ulyup.tierlist.Routes
import com.ulyup.tierlist.data.network.util.apiCall
import com.ulyup.tierlist.dto.ItemDto
import com.ulyup.tierlist.dto.MoveItemRequest
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

    override suspend fun create(tierlistId: Int, bytes: ByteArray, filename: String): ItemDto = apiCall {
        val body = MultiPartFormDataContent(
            formData {
                append(
                    key = MULTIPART_IMAGE_PART,
                    value = bytes,
                    headers = Headers.build {
                        append(HttpHeaders.ContentType, filenameToContentType(filename))
                        append(HttpHeaders.ContentDisposition, "filename=\"$filename\"")
                    },
                )
            }
        )
        httpClient.post(Routes.Items.root(tierlistId)) { setBody(body) }.body()
    }

    override suspend fun delete(tierlistId: Int, itemId: Int) {
        apiCall { httpClient.delete(Routes.Items.byId(tierlistId, itemId)) }
    }

    override suspend fun move(tierlistId: Int, itemId: Int, request: MoveItemRequest): ItemDto = apiCall {
        httpClient.patch(Routes.Items.move(tierlistId, itemId)) { setBody(request) }.body()
    }

    private fun filenameToContentType(filename: String): String =
        when (filename.substringAfterLast('.', "").lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "webp" -> "image/webp"
            else -> "application/octet-stream"
        }

    companion object {
        private const val MULTIPART_IMAGE_PART = "image"
    }
}
