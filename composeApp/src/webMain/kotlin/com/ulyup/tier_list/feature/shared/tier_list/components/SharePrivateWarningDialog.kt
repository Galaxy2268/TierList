package com.ulyup.tier_list.feature.shared.tier_list.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ulyup.tier_list.core.ui.components.button.LinkTextButton
import com.ulyup.tier_list.core.ui.components.button.PrimaryButton
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.share_private_warning_action_cancel
import com.ulyup.tier_list.resources.share_private_warning_action_confirm
import com.ulyup.tier_list.resources.share_private_warning_body
import com.ulyup.tier_list.resources.share_private_warning_title
import com.ulyup.tier_list.theme.appColors
import com.ulyup.tier_list.theme.appTypography
import org.jetbrains.compose.resources.stringResource

@Composable
fun SharePrivateWarningDialog(
    onMakePublic: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean = false,
) {
    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        containerColor = appColors.surface,
        title = {
            Text(
                text = stringResource(Res.string.share_private_warning_title),
                style = appTypography.titleMedium,
                color = appColors.onSurface,
            )
        },
        text = {
            Text(
                text = stringResource(Res.string.share_private_warning_body),
                style = appTypography.bodyMedium,
                color = appColors.onSurfaceVariant,
            )
        },
        confirmButton = {
            PrimaryButton(
                text = stringResource(Res.string.share_private_warning_action_confirm),
                onClick = onMakePublic,
                isLoading = isLoading,
            )
        },
        dismissButton = {
            LinkTextButton(
                text = stringResource(Res.string.share_private_warning_action_cancel),
                onClick = onDismiss,
                enabled = !isLoading,
            )
        },
    )
}
