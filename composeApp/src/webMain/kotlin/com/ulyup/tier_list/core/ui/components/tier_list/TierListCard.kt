package com.ulyup.tier_list.core.ui.components.tier_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ulyup.tier_list.core.ui.components.button.model.TierListOption
import com.ulyup.tier_list.core.ui.token.VBox4
import com.ulyup.tier_list.core.ui.token.aPadding16
import com.ulyup.tier_list.core.ui.token.roundedShape12
import com.ulyup.tier_list.core.ui.util.toIsoDate
import com.ulyup.tier_list.domain.tier_list.model.TierList
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.card_owner_indicator
import com.ulyup.tier_list.resources.ic_profile
import com.ulyup.tier_list.theme.appColors
import com.ulyup.tier_list.theme.appTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TierListCard(
    tierList: TierList,
    isOwner: Boolean,
    onClick: () -> Unit,
    onOption: (TierListOption) -> Unit,
    showOwnerIndicator: Boolean = true,
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
        border = if (tierList.isFavourite) BorderStroke(1.dp, appColors.premium) else null,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(aPadding16),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tierList.title,
                    style = appTypography.titleMedium,
                    color = appColors.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                VBox4
                Text(
                    text = tierList.createdAt.toIsoDate(),
                    style = appTypography.bodySmall,
                    color = appColors.onSurfaceVariant,
                )
            }
            if (isOwner && showOwnerIndicator) {
                Icon(
                    painter = painterResource(Res.drawable.ic_profile),
                    contentDescription = stringResource(Res.string.card_owner_indicator),
                    tint = appColors.onSurfaceVariant,
                )
            }
            TierListOptionsMenu(
                isOwner = isOwner,
                isPublic = tierList.isPublic,
                isFavourite = tierList.isFavourite,
                onOption = onOption,
            )
        }
    }
}
