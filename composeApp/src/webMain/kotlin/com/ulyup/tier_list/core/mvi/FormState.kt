package com.ulyup.tier_list.core.mvi

import org.jetbrains.compose.resources.StringResource

interface FormState<F : FormState<F>> : LoadableState<F> {
    val validationErrorRes: StringResource?

    fun copyForm(
        isLoading: Boolean,
        validationErrorRes: StringResource?,
        errorMessage: String?,
    ): F

    override fun copyLoadable(isLoading: Boolean, errorMessage: String?): F =
        copyForm(
            isLoading = isLoading,
            validationErrorRes = null,
            errorMessage = errorMessage,
        )

    fun withInputChanged(): F = copyForm(
        isLoading = isLoading,
        validationErrorRes = null,
        errorMessage = null,
    )

    fun withValidationError(res: StringResource): F = copyForm(
        isLoading = false,
        validationErrorRes = res,
        errorMessage = null,
    )
}
