package com.ulyup.tier_list.core.ui.components.tier_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ulyup.tier_list.core.ui.token.VBox4
import com.ulyup.tier_list.core.ui.token.aPadding16
import com.ulyup.tier_list.core.ui.token.roundedShape12
import com.ulyup.tier_list.core.ui.util.toIsoDate
import com.ulyup.tier_list.domain.tier_list.model.TierList
import com.ulyup.tier_list.theme.appColors
import com.ulyup.tier_list.theme.appTypography

@Composable
fun TierListCard(
    tierList: TierList,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = roundedShape12,
        colors = CardDefaults.cardColors(
            containerColor = appColors.surface,
            contentColor = appColors.onSurface,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(aPadding16),
        ) {
            Text(
                text = tierList.title,
                style = appTypography.titleMedium,
                color = appColors.onSurface,
            )
            VBox4
            Text(
                text = tierList.createdAt.toIsoDate(),
                style = appTypography.bodySmall,
                color = appColors.onSurfaceVariant,
            )
        }
    }
}
