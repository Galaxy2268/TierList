package com.ulyup.tierlist.core.ui.components.tierlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ulyup.tierlist.core.ui.token.VBox4
import com.ulyup.tierlist.core.ui.token.aPadding16
import com.ulyup.tierlist.core.ui.token.roundedShape12
import com.ulyup.tierlist.core.ui.util.toIsoDate
import com.ulyup.tierlist.domain.tierlist.model.Tierlist
import com.ulyup.tierlist.theme.appColors
import com.ulyup.tierlist.theme.appTypography

@Composable
fun TierlistCard(
    tierlist: Tierlist,
    modifier: Modifier = Modifier,
) {
    Card(
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
                text = tierlist.title,
                style = appTypography.titleMedium,
                color = appColors.onSurface,
            )
            VBox4
            Text(
                text = tierlist.createdAt.toIsoDate(),
                style = appTypography.bodySmall,
                color = appColors.onSurfaceVariant,
            )
        }
    }
}
