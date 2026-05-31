package com.ulyup.tier_list.core.ui.components.button.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ulyup.tier_list.resources.*
import com.ulyup.tier_list.theme.appColors
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class ActionVisibility { EVERYONE, LOGGED_IN, OWNER_ONLY }

enum class TierListAction(
    val labelRes: StringResource,
    val trailingLabelRes: StringResource? = null,
    val iconRes: DrawableResource,
    val trailingIconRes: DrawableResource? = null,
    val visibility: ActionVisibility,
) {
    FAVOURITE(
        labelRes = Res.string.detail_action_unfavourite,
        trailingLabelRes = Res.string.detail_action_favourite,
        iconRes = Res.drawable.ic_favourite_filled,
        trailingIconRes = Res.drawable.ic_favourite,
        visibility = ActionVisibility.LOGGED_IN,
    ),
    SHARE(
        labelRes = Res.string.share_action_label,
        iconRes = Res.drawable.ic_share,
        visibility = ActionVisibility.EVERYONE,
    ),
    VISIBILITY(
        labelRes = Res.string.detail_action_make_private,
        trailingLabelRes = Res.string.detail_action_make_public,
        iconRes = Res.drawable.ic_visible,
        trailingIconRes = Res.drawable.ic_unvisible,
        visibility = ActionVisibility.OWNER_ONLY,
    ),
    EDIT(
        labelRes = Res.string.detail_action_rename,
        iconRes = Res.drawable.ic_edit,
        visibility = ActionVisibility.OWNER_ONLY,
    ),
    CLEAR(
        labelRes = Res.string.detail_action_clear,
        iconRes = Res.drawable.ic_clear,
        visibility = ActionVisibility.OWNER_ONLY,
    ),
    DELETE(
        labelRes = Res.string.detail_action_delete,
        iconRes = Res.drawable.ic_delete,
        visibility = ActionVisibility.OWNER_ONLY,
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

    companion object{
        fun getVisibleActions(isLoggedIn: Boolean, isOwner: Boolean): List<TierListAction> = TierListAction.entries.filter { action ->
            when (action.visibility) {
                ActionVisibility.EVERYONE -> true
                ActionVisibility.LOGGED_IN -> isLoggedIn
                ActionVisibility.OWNER_ONLY -> isOwner
            }
        }
    }
}