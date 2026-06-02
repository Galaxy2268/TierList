package com.ulyup.tier_list.feature.mylists.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ulyup.tier_list.core.ui.components.button.LinkTextButton
import com.ulyup.tier_list.core.ui.components.button.PrimaryButton
import com.ulyup.tier_list.core.ui.components.text.AppTextField
import com.ulyup.tier_list.core.ui.components.text.ErrorText
import com.ulyup.tier_list.core.ui.token.VBox8
import com.ulyup.tier_list.core.ui.token.VBox16
import com.ulyup.tier_list.feature.mylists.vm.CreateDialogState
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.general_action_cancel
import com.ulyup.tier_list.resources.general_field_title
import com.ulyup.tier_list.resources.mylists_create_action_confirm
import com.ulyup.tier_list.resources.mylists_create_field_visibility
import com.ulyup.tier_list.resources.mylists_create_title
import com.ulyup.tier_list.theme.appColors
import com.ulyup.tier_list.theme.appTypography
import org.jetbrains.compose.resources.stringResource

@Composable
fun CreateTierListDialog(
    state: CreateDialogState,
    onTitleChange: (String) -> Unit,
    onPublicChange: (Boolean) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val errorText = state.validationErrorRes?.let { stringResource(it) } ?: state.errorMessage

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = appColors.surface,
        title = {
            Text(
                text = stringResource(Res.string.mylists_create_title),
                style = appTypography.titleMedium,
                color = appColors.onSurface,
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                AppTextField(
                    value = state.title,
                    onValueChange = onTitleChange,
                    label = stringResource(Res.string.general_field_title),
                    enabled = !state.isLoading,
                )
                VBox16
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(Res.string.mylists_create_field_visibility),
                        style = appTypography.bodyLarge,
                        color = appColors.onSurface,
                    )
                    Switch(
                        checked = state.isPublic,
                        onCheckedChange = onPublicChange,
                        enabled = !state.isLoading,
                    )
                }
                errorText?.let { message ->
                    VBox8
                    ErrorText(message = message)
                }
            }
        },
        confirmButton = {
            PrimaryButton(
                text = stringResource(Res.string.mylists_create_action_confirm),
                onClick = onConfirm,
                isLoading = state.isLoading,
                enabled = state.isSubmitEnabled,
            )
        },
        dismissButton = {
            LinkTextButton(
                text = stringResource(Res.string.general_action_cancel),
                onClick = onDismiss,
                enabled = !state.isLoading,
            )
        },
    )
}
