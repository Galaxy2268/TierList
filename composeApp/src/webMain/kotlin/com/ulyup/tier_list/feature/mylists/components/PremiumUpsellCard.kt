package com.ulyup.tier_list.feature.mylists.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ulyup.tier_list.core.ui.components.button.PrimaryButton
import com.ulyup.tier_list.core.ui.token.VBox8
import com.ulyup.tier_list.core.ui.token.VBox16
import com.ulyup.tier_list.core.ui.token.aPadding16
import com.ulyup.tier_list.core.ui.token.roundedShape12
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.mylists_premium_action_upgrade
import com.ulyup.tier_list.resources.mylists_premium_body
import com.ulyup.tier_list.resources.mylists_premium_title
import com.ulyup.tier_list.theme.appColors
import com.ulyup.tier_list.theme.appTypography
import org.jetbrains.compose.resources.stringResource

@Composable
fun PremiumUpsellCard(
    isUpgrading: Boolean,
    onUpgrade: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = roundedShape12,
        colors = CardDefaults.cardColors(
            containerColor = appColors.surfaceVariant,
            contentColor = appColors.onSurface,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(aPadding16),
        ) {
            Text(
                text = stringResource(Res.string.mylists_premium_title),
                style = appTypography.titleMedium,
                color = appColors.primary,
            )
            VBox8
            Text(
                text = stringResource(Res.string.mylists_premium_body),
                style = appTypography.bodyMedium,
                color = appColors.onSurfaceVariant,
            )
            VBox16
            PrimaryButton(
                text = stringResource(Res.string.mylists_premium_action_upgrade),
                onClick = onUpgrade,
                isLoading = isUpgrading,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
