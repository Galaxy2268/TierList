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
    IconButton(
        onClick = onClick,
        enabled = enabled,
    ) {
        Icon(
            painter = painterResource(action.getIcon(selected)),
            contentDescription = stringResource(action.getLabel(selected)),
            tint = action.getColor(selected),
        )
    }
}