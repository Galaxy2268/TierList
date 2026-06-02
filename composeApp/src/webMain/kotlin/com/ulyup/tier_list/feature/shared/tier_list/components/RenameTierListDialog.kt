package com.ulyup.tier_list.feature.shared.tier_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ulyup.tier_list.core.ui.components.button.LinkTextButton
import com.ulyup.tier_list.core.ui.components.button.PrimaryButton
import com.ulyup.tier_list.core.ui.components.text.AppTextField
import com.ulyup.tier_list.core.ui.components.text.ErrorText
import com.ulyup.tier_list.core.ui.token.VBox8
import com.ulyup.tier_list.feature.shared.tier_list.vm.RenameDialogState
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.detail_rename_action_confirm
import com.ulyup.tier_list.resources.general_action_cancel
import com.ulyup.tier_list.resources.general_action_rename_tier_list
import com.ulyup.tier_list.resources.general_field_title
import com.ulyup.tier_list.theme.appColors
import com.ulyup.tier_list.theme.appTypography
import org.jetbrains.compose.resources.stringResource

@Composable
fun RenameTierListDialog(
    state: RenameDialogState,
    onTitleChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val errorText = state.validationErrorRes?.let { stringResource(it) } ?: state.errorMessage

    AlertDialog(
        onDismissRequest = { if (!state.isLoading) onDismiss() },
        containerColor = appColors.surface,
        title = {
            Text(
                text = stringResource(Res.string.general_action_rename_tier_list),
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
                errorText?.let { message ->
                    VBox8
                    ErrorText(message = message)
                }
            }
        },
        confirmButton = {
            PrimaryButton(
                text = stringResource(Res.string.detail_rename_action_confirm),
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
