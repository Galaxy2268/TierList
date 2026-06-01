package com.ulyup.tier_list.feature.shared.tier_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.ulyup.tier_list.core.browser.ShareDetailLink
import com.ulyup.tier_list.core.ui.components.button.model.TierListOption
import com.ulyup.tier_list.feature.shared.tier_list.vm.ShareAction
import com.ulyup.tier_list.feature.shared.tier_list.vm.ShowClearAction
import com.ulyup.tier_list.feature.shared.tier_list.vm.ShowDeleteAction
import com.ulyup.tier_list.feature.shared.tier_list.vm.ShowRenameAction
import com.ulyup.tier_list.feature.shared.tier_list.vm.TierListOptionTarget
import com.ulyup.tier_list.feature.shared.tier_list.vm.TierListOptionsViewModel
import com.ulyup.tier_list.feature.shared.tier_list.vm.ToggleFavouriteAction
import com.ulyup.tier_list.feature.shared.tier_list.vm.ToggleVisibilityAction

@Composable
fun rememberTierListOptionDispatch(
    viewModel: TierListOptionsViewModel,
): (TierListOptionTarget, TierListOption) -> Unit {
    @Suppress("DEPRECATION")
    val clipboard = LocalClipboardManager.current
    return remember(viewModel, clipboard) {
        { target, option ->
            when (option) {
                TierListOption.SHARE -> {
                    clipboard.setText(AnnotatedString(ShareDetailLink.shareUrlFor(target.id)))
                    viewModel.onAction(ShareAction(target))
                }
                TierListOption.FAVOURITE -> viewModel.onAction(ToggleFavouriteAction(target))
                TierListOption.VISIBILITY -> viewModel.onAction(ToggleVisibilityAction(target))
                TierListOption.EDIT -> viewModel.onAction(ShowRenameAction(target))
                TierListOption.CLEAR -> viewModel.onAction(ShowClearAction(target))
                TierListOption.DELETE -> viewModel.onAction(ShowDeleteAction(target))
            }
        }
    }
}
