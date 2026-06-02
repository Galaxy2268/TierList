package com.ulyup.tier_list.core.ui.components.button

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.ulyup.tier_list.core.ui.components.button.model.TierListOption
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TierListOptionButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    option: TierListOption,
    selected: Boolean = true,
) {
    val iconPainter = painterResource(option.iconRes)
    val trailingIconPainter = painterResource(option.trailingIconRes ?: option.iconRes)
    IconButton(
        onClick = onClick,
        enabled = enabled,
    ) {
        Icon(
            painter = if (selected) iconPainter else trailingIconPainter,
            contentDescription = stringResource(option.getLabel(selected)),
            tint = option.getColor(selected),
        )
    }
}
