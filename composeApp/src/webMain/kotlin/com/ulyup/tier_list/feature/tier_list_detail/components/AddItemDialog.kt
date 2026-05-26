package com.ulyup.tier_list.feature.tier_list_detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.ulyup.tier_list.core.ui.components.button.LinkTextButton
import com.ulyup.tier_list.core.ui.components.button.PrimaryButton
import com.ulyup.tier_list.core.ui.components.text.ErrorText
import com.ulyup.tier_list.core.ui.token.VBox8
import com.ulyup.tier_list.core.ui.token.size64
import com.ulyup.tier_list.feature.tier_list_detail.vm.AddItemDialogState
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.detail_action_add_item
import com.ulyup.tier_list.resources.detail_action_cancel
import com.ulyup.tier_list.resources.detail_action_pick_image
import com.ulyup.tier_list.resources.detail_add_dialog_title
import com.ulyup.tier_list.theme.appColors
import com.ulyup.tier_list.theme.appTypography
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddItemDialog(
    state: AddItemDialogState,
    onImagePicked: (ByteArray, String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val errorText = state.validationErrorRes?.let { stringResource(it) } ?: state.errorMessage
    val scope = rememberCoroutineScope()
    val launcher = rememberFilePickerLauncher(type = FileKitType.Image) { file ->
        if (file != null) {
            scope.launch {
                onImagePicked(file.readBytes(), file.name)
            }
        }
    }

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
                LinkTextButton(
                    text = stringResource(Res.string.detail_action_pick_image),
                    onClick = { launcher.launch() },
                    enabled = !state.isLoading,
                )
                state.pickedImage?.let { picked ->
                    VBox8
                    AsyncImage(
                        model = picked.bytes,
                        contentDescription = null,
                        modifier = Modifier.size(size64),
                        contentScale = ContentScale.Crop,
                    )
                    VBox8
                    Text(
                        text = picked.filename,
                        style = appTypography.bodySmall,
                        color = appColors.onSurface,
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
                text = stringResource(Res.string.detail_action_add_item),
                onClick = onConfirm,
                isLoading = state.isLoading,
                enabled = state.isSubmitEnabled,
            )
        },
        dismissButton = {
            LinkTextButton(
                text = stringResource(Res.string.detail_action_cancel),
                onClick = onDismiss,
                enabled = !state.isLoading,
            )
        },
    )
}
