package com.ulyup.tier_list.feature.auth.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.ulyup.tier_list.core.ui.components.button.LinkTextButton
import com.ulyup.tier_list.core.ui.components.button.PrimaryButton
import com.ulyup.tier_list.core.ui.components.text.AppTextField
import com.ulyup.tier_list.core.ui.components.text.ErrorText
import com.ulyup.tier_list.core.ui.token.VBox8
import com.ulyup.tier_list.core.ui.token.VBox16

@Composable
fun AuthPasswordSection(
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordLabel: String,
    submitLabel: String,
    onSubmit: () -> Unit,
    secondaryLabel: String,
    onSecondaryClick: () -> Unit,
    errorMessage: String?,
    isLoading: Boolean,
) {
    VBox16

    AppTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = passwordLabel,
        enabled = !isLoading,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation(),
    )
    VBox16

    PrimaryButton(
        text = submitLabel,
        onClick = onSubmit,
        isLoading = isLoading,
        modifier = Modifier.fillMaxWidth(),
    )

    errorMessage?.let { message ->
        VBox8
        ErrorText(message = message)
    }

    VBox8
    LinkTextButton(
        text = secondaryLabel,
        onClick = onSecondaryClick,
        enabled = !isLoading,
    )
}