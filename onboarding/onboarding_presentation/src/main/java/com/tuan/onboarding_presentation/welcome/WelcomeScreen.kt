package com.tuan.onboarding_presentation.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.tuan.core.R
import com.tuan.core_ui.LocalSpacing
import com.tuan.core_ui.Spacing
import com.tuan.onboarding_presentation.components.ActionButton

@Composable
fun WelcomeScreen(
    onNextClick: () -> Unit
) {
    val spacing: Spacing = LocalSpacing.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = spacing.spaceMedium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.welcome_text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(height = spacing.spaceMedium))
        ActionButton(
            text = stringResource(id = R.string.lets_go),
            onClick = onNextClick,
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        )
    }
}