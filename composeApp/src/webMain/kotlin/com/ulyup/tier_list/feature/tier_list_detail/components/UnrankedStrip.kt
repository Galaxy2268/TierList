package com.ulyup.tier_list.feature.tier_list_detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import com.ulyup.tier_list.core.ui.token.gap8
import com.ulyup.tier_list.domain.tier_list.model.TierListItem
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.detail_unranked_label
import com.ulyup.tier_list.theme.appColors
import com.ulyup.tier_list.theme.appTypography
import org.jetbrains.compose.resources.stringResource

@Composable
fun UnrankedStrip(
    items: List<TierListItem>,
    modifier: Modifier = Modifier,
    onDeleteItem: ((Int) -> Unit)? = null,
    onRowPositioned: ((Rect) -> Unit)? = null,
    onItemPositioned: ((Int, Rect) -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned { coords -> onRowPositioned?.invoke(coords.boundsInWindow()) },
    ) {
        Text(
            text = stringResource(Res.string.detail_unranked_label),
            color = appColors.onSurfaceVariant,
            style = appTypography.labelLarge,
            modifier = Modifier.padding(start = gap8, bottom = gap8),
        )
        TierItemsRow(
            items = items,
            onDeleteItem = onDeleteItem,
            onItemPositioned = onItemPositioned,
        )
    }
}
