package com.ulyup.tierlist.feature.tierlist.detail.components

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
import com.ulyup.tierlist.core.ui.token.gap8
import com.ulyup.tierlist.core.ui.token.itemCellSize
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
                .heightIn(min = itemCellSize),
        ) {
            items.forEach { item -> ItemCell(item = item) }
        }
    }
}
