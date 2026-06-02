package com.ulyup.tier_list.core.ui.components.button.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ulyup.tier_list.resources.*
import com.ulyup.tier_list.theme.appColors
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class OptionVisibility { EVERYONE, OWNER_ONLY }

enum class TierListOption(
    val labelRes: StringResource,
    val trailingLabelRes: StringResource? = null,
    val iconRes: DrawableResource,
    val trailingIconRes: DrawableResource? = null,
    val visibility: OptionVisibility,
) {
    FAVOURITE(
        labelRes = Res.string.detail_action_unfavourite,
        trailingLabelRes = Res.string.detail_action_favourite,
        iconRes = Res.drawable.ic_favourite_filled,
        trailingIconRes = Res.drawable.ic_favourite,
        visibility = OptionVisibility.EVERYONE,
    ),
    SHARE(
        labelRes = Res.string.share_action_label,
        iconRes = Res.drawable.ic_share,
        visibility = OptionVisibility.EVERYONE,
    ),
    COPY(
        labelRes = Res.string.general_action_copy,
        iconRes = Res.drawable.ic_copy,
        visibility = OptionVisibility.EVERYONE,
    ),
    VISIBILITY(
        labelRes = Res.string.detail_action_make_private,
        trailingLabelRes = Res.string.general_action_make_public,
        iconRes = Res.drawable.ic_visible,
        trailingIconRes = Res.drawable.ic_unvisible,
        visibility = OptionVisibility.OWNER_ONLY,
    ),
    EDIT(
        labelRes = Res.string.general_action_rename_tier_list,
        iconRes = Res.drawable.ic_edit,
        visibility = OptionVisibility.OWNER_ONLY,
    ),
    CLEAR(
        labelRes = Res.string.detail_action_clear,
        iconRes = Res.drawable.ic_clear,
        visibility = OptionVisibility.OWNER_ONLY,
    ),
    DELETE(
        labelRes = Res.string.detail_action_delete,
        iconRes = Res.drawable.ic_delete,
        visibility = OptionVisibility.OWNER_ONLY,
    );


    fun getIcon(selected: Boolean) = if (selected) iconRes else trailingIconRes ?: iconRes

    fun getLabel(selected: Boolean) = if (selected) labelRes else trailingLabelRes ?: labelRes

    @Composable
    fun getColor(selected: Boolean) = if (selected) color() else trailingColor() ?: color()


    @Composable
    private fun color(): Color = when (this) {
        DELETE -> appColors.error
        FAVOURITE -> appColors.premium
        else -> appColors.onSurface
    }

    @Composable
    private fun trailingColor(): Color? = when (this) {
        FAVOURITE -> appColors.onSurface
        else -> null
    }

    fun isSelected(isPublic: Boolean, isFavourite: Boolean): Boolean = when (this) {
        FAVOURITE -> isFavourite
        VISIBILITY -> isPublic
        else -> true
    }

    companion object{
        fun getVisibleOptions(isOwner: Boolean): List<TierListOption> = TierListOption.entries.filter { option ->
            when (option.visibility) {
                OptionVisibility.EVERYONE -> true
                OptionVisibility.OWNER_ONLY -> isOwner
            }
        }
    }
}
