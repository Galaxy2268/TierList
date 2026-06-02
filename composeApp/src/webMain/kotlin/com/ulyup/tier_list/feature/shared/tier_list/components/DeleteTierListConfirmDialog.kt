package com.ulyup.tier_list.feature.shared.tier_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ulyup.tier_list.core.ui.components.button.LinkTextButton
import com.ulyup.tier_list.core.ui.components.text.ErrorText
import com.ulyup.tier_list.core.ui.token.VBox8
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.detail_delete_action_confirm
import com.ulyup.tier_list.resources.detail_delete_body
import com.ulyup.tier_list.resources.detail_delete_title
import com.ulyup.tier_list.resources.general_action_cancel
import com.ulyup.tier_list.theme.appColors
import com.ulyup.tier_list.theme.appTypography
import org.jetbrains.compose.resources.stringResource

@Composable
fun DeleteTierListConfirmDialog(
    tierListTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null,
) {
    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        containerColor = appColors.surface,
        title = {
            Text(
                text = stringResource(Res.string.detail_delete_title),
                style = appTypography.titleMedium,
                color = appColors.onSurface,
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(Res.string.detail_delete_body, tierListTitle),
                    style = appTypography.bodyMedium,
                    color = appColors.onSurfaceVariant,
                )
                errorMessage?.let { message ->
                    VBox8
                    ErrorText(message = message)
                }
            }
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
                    text = stringResource(Res.string.detail_delete_action_confirm),
                    style = appTypography.labelLarge,
                )
            }
        },
        dismissButton = {
            LinkTextButton(
                text = stringResource(Res.string.general_action_cancel),
                onClick = onDismiss,
                enabled = !isLoading,
            )
        },
    )
}
