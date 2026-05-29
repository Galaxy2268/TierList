package com.ulyup.tier_list.core.ui.components.button.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.detail_action_clear
import com.ulyup.tier_list.resources.detail_action_delete
import com.ulyup.tier_list.resources.detail_action_make_private
import com.ulyup.tier_list.resources.detail_action_make_public
import com.ulyup.tier_list.resources.detail_action_rename
import com.ulyup.tier_list.resources.ic_clear
import com.ulyup.tier_list.resources.ic_delete
import com.ulyup.tier_list.resources.ic_edit
import com.ulyup.tier_list.resources.ic_share
import com.ulyup.tier_list.resources.ic_unvisible
import com.ulyup.tier_list.resources.ic_visible
import com.ulyup.tier_list.resources.share_action_label
import com.ulyup.tier_list.theme.appColors
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class TierListAction(
    val labelRes: StringResource,
    val trailingLabelRes: StringResource? = null,
    val iconRes: DrawableResource,
    val trailingIconRes: DrawableResource? = null,
    val visibleToNonOwners: Boolean,
) {
    SHARE(
        labelRes = Res.string.share_action_label,
        iconRes = Res.drawable.ic_share,
        visibleToNonOwners = true,
    ),
    VISIBILITY(
        labelRes = Res.string.detail_action_make_private,
        trailingLabelRes = Res.string.detail_action_make_public,
        iconRes = Res.drawable.ic_visible,
        trailingIconRes = Res.drawable.ic_unvisible,
        visibleToNonOwners = false,
    ),
    EDIT(
        labelRes = Res.string.detail_action_rename,
        iconRes = Res.drawable.ic_edit,
        visibleToNonOwners = false,
    ),
    CLEAR(
        labelRes = Res.string.detail_action_clear,
        iconRes = Res.drawable.ic_clear,
        visibleToNonOwners = false,
    ),
    DELETE(
        labelRes = Res.string.detail_action_delete,
        iconRes = Res.drawable.ic_delete,
        visibleToNonOwners = false,
    );


    @Composable
    fun color(): Color = when (this) {
        DELETE -> appColors.error
        else -> appColors.onSurface
    }
}