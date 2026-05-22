package com.ulyup.tierlist.feature.tierlist.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.ulyup.tierlist.core.ui.token.size64
import com.ulyup.tierlist.core.ui.token.size14
import com.ulyup.tierlist.core.ui.token.size20
import com.ulyup.tierlist.domain.tierlist.model.Item
import com.ulyup.tierlist.resources.Res
import com.ulyup.tierlist.resources.detail_action_delete_item
import com.ulyup.tierlist.resources.ic_delete
import com.ulyup.tierlist.theme.appColors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ItemCell(
    item: Item,
    modifier: Modifier = Modifier,
    onDelete: (() -> Unit)? = null,
) {
    Box(modifier = modifier.size(size64)) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = null,
            modifier = Modifier.size(size64),
            contentScale = ContentScale.Crop,
        )
        if (onDelete != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(size20)
                    .clip(CircleShape)
                    .background(appColors.surface.copy(alpha = 0.7f))
                    .clickable(onClick = onDelete),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = stringResource(Res.string.detail_action_delete_item),
                    tint = appColors.onSurface,
                    modifier = Modifier.size(size14),
                )
            }
        }
    }
}
