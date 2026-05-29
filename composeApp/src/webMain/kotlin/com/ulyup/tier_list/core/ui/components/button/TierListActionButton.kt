package com.ulyup.tier_list.core.ui.components.button

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.ulyup.tier_list.core.ui.components.button.model.TierListAction
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TierListActionButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    action: TierListAction,
    selected: Boolean = true,
) {
    val icon = if (selected) action.iconRes else action.trailingIconRes ?: action.iconRes
    val label = if (selected) action.labelRes else action.trailingLabelRes ?: action.labelRes
    val tint = if (selected) action.color() else action.trailingColor() ?: action.color()
    IconButton(
        onClick = onClick,
        enabled = enabled,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = stringResource(label),
            tint = tint,
        )
    }
}