package com.ulyup.tier_list.feature.tier_list_detail.components

import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import com.ulyup.tier_list.core.ui.components.button.DeleteButton
import com.ulyup.tier_list.core.ui.components.image.AppImage
import com.ulyup.tier_list.core.ui.token.size96
import com.ulyup.tier_list.domain.tier_list.model.TierListItem
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.detail_action_delete_item
import org.jetbrains.compose.resources.stringResource

@Composable
fun TierListItem(
    item: TierListItem,
    modifier: Modifier = Modifier,
    onDelete: (() -> Unit)? = null,
    onPositioned: ((Rect) -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(
        modifier = modifier
            .size(size96)
            .hoverable(interactionSource)
            .onGloballyPositioned { coords -> onPositioned?.invoke(coords.boundsInWindow()) },
    ) {
        AppImage(
            model = item.imageUrl,
            contentDescription = null,
            size = size96,
            resolutionScale = IMAGE_RESOLUTION_SCALE,
        )
        if (onDelete != null && isHovered) {
            DeleteButton(
                onClick = onDelete,
                contentDescription = stringResource(Res.string.detail_action_delete_item),
                modifier = Modifier.align(Alignment.TopEnd),
            )
        }
    }
}

// Decode at 4× the display size — gives the high-quality filter a higher-res source
// to downscale from, capping memory regardless of upload dimensions.
private const val IMAGE_RESOLUTION_SCALE = 4
