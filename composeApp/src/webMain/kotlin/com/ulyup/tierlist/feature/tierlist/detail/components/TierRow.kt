package com.ulyup.tierlist.feature.tierlist.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ulyup.tierlist.core.ui.token.itemCellSize
import com.ulyup.tierlist.domain.tierlist.model.Item
import com.ulyup.tierlist.model.Tier
import com.ulyup.tierlist.resources.Res
import com.ulyup.tierlist.resources.tier_label_a
import com.ulyup.tierlist.resources.tier_label_b
import com.ulyup.tierlist.resources.tier_label_c
import com.ulyup.tierlist.resources.tier_label_d
import com.ulyup.tierlist.resources.tier_label_f
import com.ulyup.tierlist.resources.tier_label_s
import com.ulyup.tierlist.theme.appColors
import com.ulyup.tierlist.theme.appTypography
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TierRow(
    tier: Tier,
    items: List<Item>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .heightIn(min = itemCellSize),
    ) {
        TierLabel(
            tier = tier,
            modifier = Modifier.fillMaxHeight(),
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(appColors.surface)
                .heightIn(min = itemCellSize),
        ) {
            items.forEach { item -> ItemCell(item = item) }
        }
    }
}

@Composable
private fun TierLabel(
    tier: Tier,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(itemCellSize)
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
