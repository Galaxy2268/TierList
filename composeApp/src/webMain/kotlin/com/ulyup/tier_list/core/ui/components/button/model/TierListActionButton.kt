package com.ulyup.tier_list.core.ui.components.button.model

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
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
    IconButton(
        onClick = onClick,
        enabled = enabled,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = stringResource(label),
            tint = action.color(),
        )
    }
}