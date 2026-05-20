package com.ulyup.tierlist.feature.tierlist.detail.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.ulyup.tierlist.core.ui.token.itemCellSize
import com.ulyup.tierlist.domain.tierlist.model.Item

@Composable
fun ItemCell(
    item: Item,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = item.imageUrl,
        contentDescription = null,
        modifier = modifier.size(itemCellSize),
        contentScale = ContentScale.Crop,
    )
}
