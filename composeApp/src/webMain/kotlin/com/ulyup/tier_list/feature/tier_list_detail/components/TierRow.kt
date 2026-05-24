package com.ulyup.tier_list.feature.tier_list_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import com.ulyup.tier_list.core.ui.token.size96
import com.ulyup.tier_list.domain.tier_list.model.TierListItem
import com.ulyup.tier_list.model.Tier
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.tier_label_a
import com.ulyup.tier_list.resources.tier_label_b
import com.ulyup.tier_list.resources.tier_label_c
import com.ulyup.tier_list.resources.tier_label_d
import com.ulyup.tier_list.resources.tier_label_f
import com.ulyup.tier_list.resources.tier_label_s
import com.ulyup.tier_list.theme.appColors
import com.ulyup.tier_list.theme.appTypography
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TierRow(
    tier: Tier,
    items: List<TierListItem>,
    modifier: Modifier = Modifier,
    onDeleteItem: ((Int) -> Unit)? = null,
    onRowPositioned: ((Rect) -> Unit)? = null,
    onItemPositioned: ((Int, Rect) -> Unit)? = null,
    ghostImageUrl: String? = null,
    ghostIndex: Int? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .heightIn(min = size96)
            .onGloballyPositioned { coords -> onRowPositioned?.invoke(coords.boundsInWindow()) },
    ) {
        TierLabel(
            tier = tier,
            modifier = Modifier.fillMaxHeight(),
        )
        VerticalDivider(
            thickness = 2.dp,
            color = appColors.background,
        )
        TierItemsRow(
            items = items,
            onDeleteItem = onDeleteItem,
            onItemPositioned = onItemPositioned,
            ghostImageUrl = ghostImageUrl,
            ghostIndex = ghostIndex,
        )
    }
}

@Composable
private fun TierLabel(
    tier: Tier,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(size96)
            .background(appColors.tierColor(tier)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(tier.labelRes()),
            color = appColors.onTier,
            style = appTypography.titleLarge,
        )
    }
}

private fun Tier.labelRes(): StringResource = when (this) {
    Tier.S -> Res.string.tier_label_s
    Tier.A -> Res.string.tier_label_a
    Tier.B -> Res.string.tier_label_b
    Tier.C -> Res.string.tier_label_c
    Tier.D -> Res.string.tier_label_d
    Tier.F -> Res.string.tier_label_f
}
