package com.ulyup.tier_list.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ulyup.tier_list.core.ui.components.button.PrimaryButton
import com.ulyup.tier_list.core.ui.components.layout.CenteredColumn
import com.ulyup.tier_list.core.ui.components.scaffold.AppScaffold
import com.ulyup.tier_list.core.ui.token.VBox8
import com.ulyup.tier_list.core.ui.token.VBox16
import com.ulyup.tier_list.core.ui.token.VBox24
import com.ulyup.tier_list.core.ui.token.paddingV12H16
import com.ulyup.tier_list.core.ui.token.roundedShape8
import com.ulyup.tier_list.feature.profile.vm.LogoutAction
import com.ulyup.tier_list.feature.profile.vm.ProfileViewModel
import com.ulyup.tier_list.feature.profile.vm.UpgradePremiumAction
import com.ulyup.tier_list.model.Tier
import com.ulyup.tier_list.model.UserRole
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.profile_action_logout
import com.ulyup.tier_list.resources.profile_action_upgrade
import com.ulyup.tier_list.resources.profile_role_premium
import com.ulyup.tier_list.resources.profile_role_user
import com.ulyup.tier_list.resources.profile_title
import com.ulyup.tier_list.theme.appColors
import com.ulyup.tier_list.theme.appTypography
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen() {
    val viewModel = koinViewModel<ProfileViewModel>()
    val state = viewModel.uiState

    AppScaffold { padding ->
        CenteredColumn(modifier = Modifier.padding(padding)) {
            Text(
                text = stringResource(Res.string.profile_title),
                style = appTypography.titleLarge,
                color = appColors.onBackground,
            )
            state.user?.let { user ->
                VBox24
                Text(
                    text = user.username,
                    style = appTypography.titleMedium,
                    color = appColors.onBackground,
                )
                VBox8
                Text(
                    text = user.email,
                    style = appTypography.bodyMedium,
                    color = appColors.onSurfaceVariant,
                )
                VBox16
                RoleBadge(role = user.role)
            }
            if (state.showUpgradeButton) {
                VBox24
                PrimaryButton(
                    text = stringResource(Res.string.profile_action_upgrade),
                    onClick = { viewModel.onAction(UpgradePremiumAction) },
                    isLoading = state.isUpgrading,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            state.errorMessage?.let { message ->
                VBox8
                Text(
                    text = message,
                    style = appTypography.bodySmall,
                    color = appColors.error,
                )
            }
            VBox24
            PrimaryButton(
                text = stringResource(Res.string.profile_action_logout),
                onClick = { viewModel.onAction(LogoutAction) },
                isLoading = state.isLoggingOut,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun RoleBadge(role: UserRole) {
    val isPremium = role == UserRole.PREMIUM
    val background = if (isPremium) appColors.tierColor(Tier.S) else appColors.surfaceVariant
    val foreground = if (isPremium) appColors.onTier else appColors.onSurfaceVariant
    val label = stringResource(
        if (isPremium) Res.string.profile_role_premium else Res.string.profile_role_user
    )
    Box(
        modifier = Modifier
            .background(background, shape = roundedShape8)
            .padding(paddingV12H16),
    ) {
        Text(
            text = label,
            style = appTypography.labelMedium,
            color = foreground,
        )
    }
}
