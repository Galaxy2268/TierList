package com.ulyup.tierlist.feature.tierlist.detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ulyup.tierlist.core.ui.components.button.LinkTextButton
import com.ulyup.tierlist.core.ui.components.button.PrimaryButton
import com.ulyup.tierlist.core.ui.components.text.AppTextField
import com.ulyup.tierlist.core.ui.components.text.ErrorText
import com.ulyup.tierlist.core.ui.token.VBox8
import com.ulyup.tierlist.feature.tierlist.detail.vm.AddItemDialogState
import com.ulyup.tierlist.resources.Res
import com.ulyup.tierlist.resources.detail_action_add_item
import com.ulyup.tierlist.resources.detail_action_cancel
import com.ulyup.tierlist.resources.detail_add_dialog_title
import com.ulyup.tierlist.resources.detail_field_image_url
import com.ulyup.tierlist.theme.appColors
import com.ulyup.tierlist.theme.appTypography
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddItemDialog(
    state: AddItemDialogState,
    onUrlChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val errorText = state.validationErrorRes?.let { stringResource(it) } ?: state.serverErrorMessage

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = appColors.surface,
        title = {
            Text(
                text = stringResource(Res.string.detail_add_dialog_title),
                style = appTypography.titleMedium,
                color = appColors.onSurface,
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                AppTextField(
                    value = state.url,
                    onValueChange = onUrlChange,
                    label = stringResource(Res.string.detail_field_image_url),
                    enabled = !state.isSubmitting,
                )
                errorText?.let { message ->
                    VBox8
                    ErrorText(message = message)
                }
            }
        },
        confirmButton = {
            PrimaryButton(
                text = stringResource(Res.string.detail_action_add_item),
                onClick = onConfirm,
                isLoading = state.isSubmitting,
            )
        },
        dismissButton = {
            LinkTextButton(
                text = stringResource(Res.string.detail_action_cancel),
                onClick = onDismiss,
                enabled = !state.isSubmitting,
            )
        },
    )
}
