package com.ulyup.tier_list.feature.tier_list_detail.components
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.unit.dp
import com.ulyup.tier_list.core.ui.components.button.DeleteButton
import com.ulyup.tier_list.core.ui.components.button.LinkTextButton
import com.ulyup.tier_list.core.ui.components.button.PrimaryButton
import com.ulyup.tier_list.core.ui.components.image.AppImage
import com.ulyup.tier_list.core.ui.components.text.ErrorText
import com.ulyup.tier_list.core.ui.token.VBox8
import com.ulyup.tier_list.core.ui.token.gap8
import com.ulyup.tier_list.core.ui.token.size16
import com.ulyup.tier_list.core.ui.token.size64
import com.ulyup.tier_list.feature.tier_list_detail.vm.AddItemDialogState
import com.ulyup.tier_list.feature.tier_list_detail.vm.PickedImage
import com.ulyup.tier_list.resources.*
import com.ulyup.tier_list.theme.appColors
import com.ulyup.tier_list.theme.appTypography
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddItemDialog(
    state: AddItemDialogState,
    onImagesPicked: (List<PickedImage>) -> Unit,
    onRemovePicked: (Int) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val errorText = state.validationErrorRes?.let { stringResource(it) } ?: state.errorMessage
    val scope = rememberCoroutineScope()
    val launcher = rememberFilePickerLauncher(
        type = FileKitType.File("jpg", "jpeg", "png", "webp"),
        mode = FileKitMode.Multiple(),
    ) { files: List<PlatformFile>? ->
        if (!files.isNullOrEmpty()) {
            scope.launch {
                val picked = files.map { file ->
                    async { PickedImage(bytes = file.readBytes(), filename = file.name) }
                }.awaitAll()
                onImagesPicked(picked)
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
                    contentPadding = PaddingValues(horizontal = 0.dp, vertical = gap8),
                )
                Text(
                    text = stringResource(Res.string.detail_add_dialog_limit),
                    style = appTypography.bodySmall,
                    color = appColors.onSurfaceVariant,
                )
                if (state.pickedImages.isNotEmpty()) {
                    VBox8
                    PickedImagesRow(
                        images = state.pickedImages,
                        onRemove = onRemovePicked,
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
                text = stringResource(Res.string.detail_action_add_item),
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

@Composable
private fun PickedImagesRow(
    images: List<PickedImage>,
    onRemove: (Int) -> Unit,
    enabled: Boolean,
) {
    val removeLabel = stringResource(Res.string.detail_action_remove_picked_image)
    val listState = rememberLazyListState()
    val scrollbarStyle = LocalScrollbarStyle.current.copy(
        unhoverColor = appColors.onSurface.copy(alpha = 0.4f),
        hoverColor = appColors.onSurface.copy(alpha = 0.7f),
    )
    Column(modifier = Modifier.fillMaxWidth()) {
        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(gap8),
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(images.size, key = { index -> images[index].id }) { index ->
                val image = images[index]
                val interactionSource = remember { MutableInteractionSource() }
                val isHovered by interactionSource.collectIsHoveredAsState()
                Box(
                    modifier = Modifier
                        .size(size64)
                        .hoverable(interactionSource),
                ) {
                    AppImage(
                        model = image.bytes,
                        contentDescription = null,
                        size = size64,
                        filterQuality = FilterQuality.High,
                        cacheKey = image.id.toString(),
                    )
                    if (enabled && isHovered) {
                        DeleteButton(
                            onClick = { onRemove(index) },
                            contentDescription = removeLabel,
                            modifier = Modifier.align(Alignment.TopEnd),
                            size = size16,
                        )
                    }
                }
            }
        }
        HorizontalScrollbar(
            adapter = rememberScrollbarAdapter(listState),
            modifier = Modifier.fillMaxWidth(),
            style = scrollbarStyle,
        )
    }
}
