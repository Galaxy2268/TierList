package com.ulyup.tier_list.core.ui.components.image

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.size.Size

@Composable
fun AppImage(
    model: Any?,
    contentDescription: String?,
    size: Dp,
    resolutionScale: Int = 1,
    filterQuality: FilterQuality = FilterQuality.High,
    cacheKey: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val context = LocalPlatformContext.current
    val decodePx = (size.value * resolutionScale).toInt()
    val request = remember(model, decodePx, cacheKey) {
        ImageRequest.Builder(context)
            .data(model)
            .size(Size(decodePx, decodePx))
            .apply { cacheKey?.let { memoryCacheKey(it) } }
            .build()
    }
    AsyncImage(
        model = request,
        contentDescription = contentDescription,
        modifier = Modifier.size(size),
        contentScale = contentScale,
        filterQuality = filterQuality,
    )
}
