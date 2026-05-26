package com.ulyup.tier_list.feature.profile.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ulyup.tier_list.core.ui.components.button.LinkTextButton
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.profile_logout_action_cancel
import com.ulyup.tier_list.resources.profile_logout_action_confirm
import com.ulyup.tier_list.resources.profile_logout_body
import com.ulyup.tier_list.resources.profile_logout_title
import com.ulyup.tier_list.theme.appColors
import com.ulyup.tier_list.theme.appTypography
import org.jetbrains.compose.resources.stringResource

@Composable
fun LogoutConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean = false,
) {
    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        containerColor = appColors.surface,
        title = {
            Text(
                text = stringResource(Res.string.profile_logout_title),
                style = appTypography.titleMedium,
                color = appColors.onSurface,
            )
        },
        text = {
            Text(
                text = stringResource(Res.string.profile_logout_body),
                style = appTypography.bodyMedium,
                color = appColors.onSurfaceVariant,
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = appColors.error,
                    contentColor = appColors.onError,
                ),
            ) {
                Text(
                    text = stringResource(Res.string.profile_logout_action_confirm),
                    style = appTypography.labelLarge,
                )
            }
        },
        dismissButton = {
            LinkTextButton(
                text = stringResource(Res.string.profile_logout_action_cancel),
                onClick = onDismiss,
                enabled = !isLoading,
            )
        },
    )
}
