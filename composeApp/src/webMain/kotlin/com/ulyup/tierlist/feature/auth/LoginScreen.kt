package com.ulyup.tierlist.feature.auth

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ulyup.tierlist.core.ui.components.text.AppTextField
import com.ulyup.tierlist.core.ui.components.scaffold.AppScaffold
import com.ulyup.tierlist.core.ui.token.VBox24
import com.ulyup.tierlist.feature.auth.components.AuthCenteredColumn
import com.ulyup.tierlist.feature.auth.components.AuthPasswordSection
import com.ulyup.tierlist.feature.auth.vm.login.ChangePasswordAction
import com.ulyup.tierlist.feature.auth.vm.login.ChangeUsernameOrEmailAction
import com.ulyup.tierlist.feature.auth.vm.login.LoginViewModel
import com.ulyup.tierlist.feature.auth.vm.login.SubmitAction
import com.ulyup.tierlist.resources.Res
import com.ulyup.tierlist.resources.login_action_go_to_register
import com.ulyup.tierlist.resources.login_action_submit
import com.ulyup.tierlist.resources.login_field_password
import com.ulyup.tierlist.resources.login_field_username_or_email
import com.ulyup.tierlist.resources.login_title
import com.ulyup.tierlist.theme.appColors
import com.ulyup.tierlist.theme.appTypography
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
) {
    val viewModel = koinViewModel<LoginViewModel>()
    val state = viewModel.uiState

    AppScaffold { padding ->
        AuthCenteredColumn(modifier = Modifier.padding(padding)) {
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
                passwordLabel = stringResource(Res.string.login_field_password),
                submitLabel = stringResource(Res.string.login_action_submit),
                onSubmit = { viewModel.onAction(SubmitAction) },
                secondaryLabel = stringResource(Res.string.login_action_go_to_register),
                onSecondaryClick = onNavigateToRegister,
                errorMessage = state.errorMessage,
                isLoading = state.isLoading,
            )
        }
    }
}