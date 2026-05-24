package com.ulyup.tier_list.feature.tier_list_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
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
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .background(appColors.surface)
            .heightIn(min = size96),
    ) {
        items.forEach { item ->
            TierListItem(
                item = item,
                onDelete = onDeleteItem?.let { delete -> { delete(item.id) } },
                onPositioned = onItemPositioned?.let { positioned -> { rect -> positioned(item.id, rect) } },
            )
        }
    }
}