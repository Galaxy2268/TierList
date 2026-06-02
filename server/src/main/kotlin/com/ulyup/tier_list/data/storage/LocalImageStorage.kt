package com.ulyup.tier_list.data.storage

import com.ulyup.tier_list.domain.storage.ImageStorage
import com.ulyup.tier_list.domain.storage.ImageUpload
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
        val filename = filenameOrNull(url) ?: return@withContext
        File(baseDir, filename).delete()
    }

    override suspend fun copy(sourceUrl: String): String = withContext(Dispatchers.IO) {
        val filename = filenameOrNull(sourceUrl)
            ?: throw IllegalArgumentException("Cannot copy image with url: $sourceUrl")
        baseDir.mkdirs()
        val extension = filename.substringAfterLast('.', "bin")
        val newFilename = "${UUID.randomUUID()}.$extension"
        val destination = File(baseDir, newFilename)
        try {
            File(baseDir, filename).copyTo(destination)
        } catch (exception: Exception) {
            // copyTo can leave a half-written file if it fails mid-stream; the caller's
            // rollback can't see this URL yet, so clean it up here before rethrowing.
            destination.delete()
            throw exception
        }
        "$urlPrefix/$newFilename"
    }

    private fun filenameOrNull(url: String): String? {
        val prefix = "$urlPrefix/"
        if (!url.startsWith(prefix)) return null
        val filename = url.removePrefix(prefix)
        if (filename.contains('/') || filename.contains('\\') || filename.contains("..")) return null
        return filename
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
