package com.ulyup.tier_list.feature.tier_list_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Rect
import com.ulyup.tier_list.core.ui.components.image.AppImage
import com.ulyup.tier_list.core.ui.token.size96
import com.ulyup.tier_list.domain.tier_list.model.TierListItem
import com.ulyup.tier_list.theme.appColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TierItemsRow(
    items: List<TierListItem>,
    modifier: Modifier = Modifier,
    onDeleteItem: ((Int) -> Unit)? = null,
    onItemPositioned: ((Int, Rect) -> Unit)? = null,
    ghostImageUrl: String? = null,
    ghostIndex: Int? = null,
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .background(appColors.surface)
            .heightIn(min = size96),
    ) {
        items.forEachIndexed { index, item ->
            if (ghostImageUrl != null && ghostIndex == index) {
                GhostItem(imageUrl = ghostImageUrl)
            }
            TierListItem(
                item = item,
                onDelete = onDeleteItem?.let { delete -> { delete(item.id) } },
                onPositioned = onItemPositioned?.let { positioned -> { rect -> positioned(item.id, rect) } },
            )
        }
        if (ghostImageUrl != null && ghostIndex == items.size) {
            GhostItem(imageUrl = ghostImageUrl)
        }
    }
}

@Composable
private fun GhostItem(imageUrl: String) {
    Box(modifier = Modifier.size(size96).alpha(GHOST_ALPHA)) {
        AppImage(
            model = imageUrl,
            contentDescription = null,
            size = size96,
            resolutionScale = GHOST_RESOLUTION_SCALE,
        )
    }
}

private const val GHOST_ALPHA = 0.4f
private const val GHOST_RESOLUTION_SCALE = 4
