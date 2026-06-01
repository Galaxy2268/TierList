package com.ulyup.tier_list.core.ui.components.tier_list

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ulyup.tier_list.core.ui.components.button.model.TierListOption
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.card_action_more
import com.ulyup.tier_list.resources.ic_more
import com.ulyup.tier_list.theme.appColors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TierListOptionsMenu(
    isOwner: Boolean,
    isPublic: Boolean,
    isFavourite: Boolean,
    onOption: (TierListOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                painter = painterResource(Res.drawable.ic_more),
                contentDescription = stringResource(Res.string.card_action_more),
                tint = appColors.onSurface,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = appColors.surface,
        ) {
            TierListOption.getVisibleOptions(isOwner).forEach { option ->
                val selected = option.isSelected(isPublic = isPublic, isFavourite = isFavourite)
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(option.getLabel(selected)),
                            color = option.getColor(selected),
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(option.getIcon(selected)),
                            contentDescription = null,
                            tint = option.getColor(selected),
                        )
                    },
                    onClick = {
                        expanded = false
                        onOption(option)
                    },
                )
            }
        }
    }
}
