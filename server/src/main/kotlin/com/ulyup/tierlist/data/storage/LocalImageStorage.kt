package com.ulyup.tierlist.data.storage

import com.ulyup.tierlist.domain.storage.ImageStorage
import com.ulyup.tierlist.domain.storage.ImageUpload
import io.ktor.http.ContentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

class LocalImageStorage(
    private val baseDir: File = File(BASE_DIR),
    private val urlPrefix: String = URL_PREFIX,
) : ImageStorage {

    override suspend fun save(upload: ImageUpload): String = withContext(Dispatchers.IO) {
        baseDir.mkdirs()
        val filename = "${UUID.randomUUID()}.${contentTypeToExtension(upload.contentType)}"
        File(baseDir, filename).writeBytes(upload.bytes)
        "$urlPrefix/$filename"
    }

    override suspend fun delete(url: String): Unit = withContext(Dispatchers.IO) {
        val prefix = "$urlPrefix/"
        if (!url.startsWith(prefix)) return@withContext
        val filename = url.removePrefix(prefix)
        if (filename.contains('/') || filename.contains('\\') || filename.contains("..")) return@withContext
        File(baseDir, filename).delete()
    }

    // Inverse of ItemApiImpl.filenameToContentType on the client — keep tables in sync.
    private fun contentTypeToExtension(contentType: ContentType): String =
        when (contentType.contentSubtype.lowercase()) {
            "jpeg", "jpg" -> "jpg"
            "png" -> "png"
            "webp" -> "webp"
            else -> "bin"
        }

    companion object {
        const val URL_PREFIX = "/uploads"
        const val BASE_DIR = "data/uploads"
    }
}
