package com.ulyup.tier_list.core.ui.components.button.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.detail_action_clear
import com.ulyup.tier_list.resources.detail_action_delete
import com.ulyup.tier_list.resources.detail_action_favourite
import com.ulyup.tier_list.resources.detail_action_make_private
import com.ulyup.tier_list.resources.detail_action_make_public
import com.ulyup.tier_list.resources.detail_action_rename
import com.ulyup.tier_list.resources.detail_action_unfavourite
import com.ulyup.tier_list.resources.ic_clear
import com.ulyup.tier_list.resources.ic_delete
import com.ulyup.tier_list.resources.ic_edit
import com.ulyup.tier_list.resources.ic_favourite
import com.ulyup.tier_list.resources.ic_favourite_filled
import com.ulyup.tier_list.resources.ic_share
import com.ulyup.tier_list.resources.ic_unvisible
import com.ulyup.tier_list.resources.ic_visible
import com.ulyup.tier_list.resources.share_action_label
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


    @Composable
    fun color(): Color = when (this) {
        DELETE -> appColors.error
        FAVOURITE -> appColors.premium
        else -> appColors.onSurface
    }

    @Composable
    fun trailingColor(): Color? = when (this) {
        FAVOURITE -> appColors.onSurface
        else -> null
    }
}