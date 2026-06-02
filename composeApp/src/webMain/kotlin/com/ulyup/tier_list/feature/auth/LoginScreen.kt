package com.ulyup.tier_list.feature.auth

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ulyup.tier_list.core.ui.components.layout.CenteredColumn
import com.ulyup.tier_list.core.ui.components.text.AppTextField
import com.ulyup.tier_list.core.ui.components.scaffold.AppScaffold
import com.ulyup.tier_list.core.ui.token.VBox24
import com.ulyup.tier_list.feature.auth.components.AuthPasswordSection
import com.ulyup.tier_list.feature.auth.vm.login.ChangePasswordAction
import com.ulyup.tier_list.feature.auth.vm.login.ChangeUsernameOrEmailAction
import com.ulyup.tier_list.feature.auth.vm.login.LoginViewModel
import com.ulyup.tier_list.feature.auth.vm.login.SubmitAction
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.general_field_password
import com.ulyup.tier_list.resources.login_action_go_to_register
import com.ulyup.tier_list.resources.login_action_submit
import com.ulyup.tier_list.resources.login_field_username_or_email
import com.ulyup.tier_list.resources.login_title
import com.ulyup.tier_list.theme.appColors
import com.ulyup.tier_list.theme.appTypography
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
) {
    val viewModel = koinViewModel<LoginViewModel>()
    val state = viewModel.uiState

    AppScaffold { padding ->
        CenteredColumn(modifier = Modifier.padding(padding)) {
            Text(
                text = stringResource(Res.string.login_title),
                style = appTypography.titleLarge,
                color = appColors.onBackground,
            )
            VBox24

            AppTextField(
                value = state.usernameOrEmail,
                onValueChange = { viewModel.onAction(ChangeUsernameOrEmailAction(it)) },
                label = stringResource(Res.string.login_field_username_or_email),
                enabled = !state.isLoading,
            )

            AuthPasswordSection(
                password = state.password,
                onPasswordChange = { viewModel.onAction(ChangePasswordAction(it)) },
                passwordLabel = stringResource(Res.string.general_field_password),
                submitLabel = stringResource(Res.string.login_action_submit),
                onSubmit = { viewModel.onAction(SubmitAction) },
                secondaryLabel = stringResource(Res.string.login_action_go_to_register),
                onSecondaryClick = onNavigateToRegister,
                errorMessage = state.errorMessage,
                isLoading = state.isLoading,
                isSubmitEnabled = state.isSubmitEnabled,
            )
        }
    }
}