package com.ulyup.tier_list.core.ui.components.tier_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ulyup.tier_list.core.ui.token.VBox4
import com.ulyup.tier_list.core.ui.token.aPadding16
import com.ulyup.tier_list.core.ui.token.roundedShape12
import com.ulyup.tier_list.core.ui.util.toIsoDate
import com.ulyup.tier_list.domain.tier_list.model.TierList
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.detail_action_delete
import com.ulyup.tier_list.resources.ic_delete
import com.ulyup.tier_list.theme.appColors
import com.ulyup.tier_list.theme.appTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TierListCard(
    tierList: TierList,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onDelete: (() -> Unit)? = null,
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
                )
                VBox4
                Text(
                    text = tierList.createdAt.toIsoDate(),
                    style = appTypography.bodySmall,
                    color = appColors.onSurfaceVariant,
                )
            }
            if (onDelete != null) {
                IconButton(onClick = onDelete) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_delete),
                        contentDescription = stringResource(Res.string.detail_action_delete),
                        tint = appColors.error,
                    )
                }
            }
        }
    }
}
