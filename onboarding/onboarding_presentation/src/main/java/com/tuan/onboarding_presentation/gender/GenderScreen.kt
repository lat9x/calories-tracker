package com.tuan.onboarding_presentation.gender


import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.tuan.core.R
import com.tuan.core.domain.model.Gender
import com.tuan.core.util.UiEvent
import com.tuan.core_ui.LocalSpacing
import com.tuan.onboarding_presentation.components.ActionButton
import com.tuan.onboarding_presentation.components.SelectableButton
import kotlinx.coroutines.flow.collect

@Composable
fun GenderScreen(
    onNextClick: () -> Unit,
    viewModel: GenderViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current

    // collect ui events from view model
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ToNextScreen -> onNextClick()
                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = spacing.spaceLarge)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.whats_your_gender),
                style = MaterialTheme.typography.h5
            )

            Spacer(modifier = Modifier.height(height = spacing.spaceMedium))

            // 2 selectable button to choose gender
            Row {
                SelectableButton(
                    text = stringResource(id = R.string.male),
                    isSelected = viewModel.selectedGender is Gender.Male,
                    color = MaterialTheme.colors.primaryVariant,
                    selectedTextColor = Color.White,
                    onClick = {
                        viewModel.onGenderClick(gender = Gender.Male)
                    },
                    textStyle = MaterialTheme.typography.button.copy(
                        fontWeight = FontWeight.Normal
                    )
                )

                Spacer(modifier = Modifier.width(width = spacing.spaceMedium))

                SelectableButton(
                    text = stringResource(id = R.string.female),
                    isSelected = viewModel.selectedGender is Gender.Female,
                    color = MaterialTheme.colors.primaryVariant,
                    selectedTextColor = Color.White,
                    onClick = {
                        viewModel.onGenderClick(gender = Gender.Female)
                    },
                    textStyle = MaterialTheme.typography.button.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }

        ActionButton(
            text = stringResource(id = R.string.next),
            onClick = viewModel::onNextClick,
            modifier = Modifier.align(alignment = Alignment.BottomEnd)
        )
    }
}