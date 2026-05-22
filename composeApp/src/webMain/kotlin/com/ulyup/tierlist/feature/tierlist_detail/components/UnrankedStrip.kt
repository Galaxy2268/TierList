package com.ulyup.tierlist.feature.tierlist_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import com.ulyup.tierlist.core.ui.token.gap8
import com.ulyup.tierlist.core.ui.token.size64
import com.ulyup.tierlist.domain.tierlist.model.Item
import com.ulyup.tierlist.resources.Res
import com.ulyup.tierlist.resources.detail_unranked_label
import com.ulyup.tierlist.theme.appColors
import com.ulyup.tierlist.theme.appTypography
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UnrankedStrip(
    items: List<Item>,
    modifier: Modifier = Modifier,
    onDeleteItem: ((Int) -> Unit)? = null,
    onRowPositioned: ((Rect) -> Unit)? = null,
    onItemPositioned: ((Int, Rect) -> Unit)? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.detail_unranked_label),
            color = appColors.onSurfaceVariant,
            style = appTypography.labelLarge,
            modifier = Modifier.padding(start = gap8, bottom = gap8),
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(appColors.surface)
                .heightIn(min = size64)
                .onGloballyPositioned { coords -> onRowPositioned?.invoke(coords.boundsInWindow()) },
        ) {
            items.forEach { item ->
                ItemCell(
                    item = item,
                    onDelete = onDeleteItem?.let { delete -> { delete(item.id) } },
                    onPositioned = onItemPositioned?.let { positioned -> { rect -> positioned(item.id, rect) } },
                )
            }
        }
    }
}
