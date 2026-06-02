package com.ulyup.tier_list.core.ui.components.tier_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ulyup.tier_list.core.ui.components.text.AppTextField
import com.ulyup.tier_list.core.ui.token.HBox8
import com.ulyup.tier_list.core.ui.token.gap12
import com.ulyup.tier_list.core.ui.token.size20
import com.ulyup.tier_list.domain.tier_list.model.TierListSort
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.filter_clear_search
import com.ulyup.tier_list.resources.filter_favourites_only
import com.ulyup.tier_list.resources.filter_search_placeholder
import com.ulyup.tier_list.resources.filter_sort_label
import com.ulyup.tier_list.resources.filter_sort_newest
import com.ulyup.tier_list.resources.filter_sort_oldest
import com.ulyup.tier_list.resources.filter_sort_title_asc
import com.ulyup.tier_list.resources.filter_sort_title_desc
import com.ulyup.tier_list.resources.ic_arrow_drop_down
import com.ulyup.tier_list.resources.ic_check
import com.ulyup.tier_list.resources.ic_close
import com.ulyup.tier_list.resources.ic_favourite
import com.ulyup.tier_list.resources.ic_favourite_filled
import com.ulyup.tier_list.resources.ic_search
import com.ulyup.tier_list.resources.ic_sort
import com.ulyup.tier_list.theme.appColors
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TierListFilterBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    sortOrder: TierListSort,
    onSortOrderChange: (TierListSort) -> Unit,
    favouritesOnly: Boolean,
    onToggleFavouritesOnly: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val favouriteIcon = painterResource(Res.drawable.ic_favourite)
    val favouriteFilledIcon = painterResource(Res.drawable.ic_favourite_filled)
    val clearIcon = painterResource(Res.drawable.ic_close)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(gap12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = stringResource(Res.string.filter_search_placeholder),
            modifier = Modifier.weight(1f),
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = null,
                )
            },
            trailingIcon = if (searchQuery.isNotEmpty()) {
                {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            painter = clearIcon,
                            contentDescription = stringResource(Res.string.filter_clear_search),
                        )
                    }
                }
            } else {
                null
            },
        )
        SortDropdown(
            sortOrder = sortOrder,
            onSortOrderChange = onSortOrderChange,
        )
        FilterChip(
            selected = favouritesOnly,
            onClick = onToggleFavouritesOnly,
            label = { Text(stringResource(Res.string.filter_favourites_only)) },
            leadingIcon = {
                Icon(
                    painter = if (favouritesOnly) favouriteFilledIcon else favouriteIcon,
                    contentDescription = null,
                    tint = if (favouritesOnly) appColors.premium else appColors.onSurfaceVariant,
                )
            },
        )
    }
}

@Composable
private fun SortDropdown(
    sortOrder: TierListSort,
    onSortOrderChange: (TierListSort) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Icon(
                painter = painterResource(Res.drawable.ic_sort),
                contentDescription = null,
                modifier = Modifier.size(size20),
            )
            HBox8
            Text(stringResource(Res.string.filter_sort_label, stringResource(sortOrder.labelRes)))
            HBox8
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_drop_down),
                contentDescription = null,
                modifier = Modifier.size(size20),
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = appColors.surface,
        ) {
            TierListSort.entries.forEach { sort ->
                DropdownMenuItem(
                    text = { Text(stringResource(sort.labelRes)) },
                    trailingIcon = if (sort == sortOrder) {
                        {
                            Icon(
                                painter = painterResource(Res.drawable.ic_check),
                                contentDescription = null,
                                modifier = Modifier.size(size20),
                            )
                        }
                    } else {
                        null
                    },
                    onClick = {
                        expanded = false
                        onSortOrderChange(sort)
                    },
                )
            }
        }
    }
}

private val TierListSort.labelRes: StringResource
    get() = when (this) {
        TierListSort.NEWEST -> Res.string.filter_sort_newest
        TierListSort.OLDEST -> Res.string.filter_sort_oldest
        TierListSort.TITLE_ASC -> Res.string.filter_sort_title_asc
        TierListSort.TITLE_DESC -> Res.string.filter_sort_title_desc
    }
