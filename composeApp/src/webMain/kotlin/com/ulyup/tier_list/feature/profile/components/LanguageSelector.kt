package com.ulyup.tier_list.feature.profile.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ulyup.tier_list.core.ui.token.HBox8
import com.ulyup.tier_list.core.ui.token.size20
import com.ulyup.tier_list.domain.language.AppLanguage
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.ic_arrow_drop_down
import com.ulyup.tier_list.resources.ic_check
import com.ulyup.tier_list.resources.ic_language
import com.ulyup.tier_list.resources.language_english
import com.ulyup.tier_list.resources.language_latvian
import com.ulyup.tier_list.resources.profile_language_label
import com.ulyup.tier_list.theme.appColors
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LanguageSelector(
    currentLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier) {
        OutlinedButton(onClick = { expanded = true }) {
            Icon(
                painter = painterResource(Res.drawable.ic_language),
                contentDescription = stringResource(Res.string.profile_language_label),
                modifier = Modifier.size(size20),
            )
            HBox8
            Text(stringResource(currentLanguage.labelRes))
            HBox8
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_drop_down),
                contentDescription = null,
                modifier = Modifier.size(size20),
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = appColors.surface,
        ) {
            AppLanguage.entries.forEach { language ->
                DropdownMenuItem(
                    text = { Text(stringResource(language.labelRes)) },
                    trailingIcon = if (language == currentLanguage) {
                        {
                            Icon(
                                painter = painterResource(Res.drawable.ic_check),
                                contentDescription = null,
                                modifier = Modifier.size(size20),
                            )
                        }
                    } else {
                        null
                    },
                    onClick = {
                        expanded = false
                        onLanguageChange(language)
                    },
                )
            }
        }
    }
}

private val AppLanguage.labelRes: StringResource
    get() = when (this) {
        AppLanguage.ENGLISH -> Res.string.language_english
        AppLanguage.LATVIAN -> Res.string.language_latvian
    }
