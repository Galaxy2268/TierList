package com.ulyup.tier_list.feature.shared.tier_list.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ulyup.tier_list.core.ui.components.button.LinkTextButton
import com.ulyup.tier_list.core.ui.components.button.PrimaryButton
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.general_action_close
import com.ulyup.tier_list.resources.general_action_upgrade
import com.ulyup.tier_list.resources.mylists_premium_body
import com.ulyup.tier_list.resources.premium_limit_title
import com.ulyup.tier_list.theme.appColors
import com.ulyup.tier_list.theme.appTypography
import org.jetbrains.compose.resources.stringResource

@Composable
fun PremiumLimitDialog(
    onUpgrade: () -> Unit,
    onDismiss: () -> Unit,
    isUpgrading: Boolean = false,
) {
    AlertDialog(
        onDismissRequest = { if (!isUpgrading) onDismiss() },
        containerColor = appColors.surface,
        title = {
            Text(
                text = stringResource(Res.string.premium_limit_title),
                style = appTypography.titleMedium,
                color = appColors.onSurface,
            )
        },
        text = {
            Text(
                text = stringResource(Res.string.mylists_premium_body),
                style = appTypography.bodyMedium,
                color = appColors.onSurfaceVariant,
            )
        },
        confirmButton = {
            PrimaryButton(
                text = stringResource(Res.string.general_action_upgrade),
                onClick = onUpgrade,
                isLoading = isUpgrading,
            )
        },
        dismissButton = {
            LinkTextButton(
                text = stringResource(Res.string.general_action_close),
                onClick = onDismiss,
                enabled = !isUpgrading,
            )
        },
    )
}
