package com.uselessfacts.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.uselessfacts.Language
import com.uselessfacts.R
import com.uselessfacts.ui.theme.UselessFactsTheme
import com.uselessfacts.viewmodel.UselessFactsViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun UselessFacts() {
    val viewModel = hiltViewModel<UselessFactsViewModel>()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        UselessFactLanguageChooser(vm = viewModel)
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(3f)
        ) {
            UselessFactText(
                fact = viewModel.currentUselessFact,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_xsmall))
            )
            UselessFactSource(
                source = viewModel.currentFactSource,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_large))
            )
            NextFactButton(vm = viewModel,
                onClick = {
                    viewModel.getNextFact()
                    viewModel.enableButton(false)
                    CoroutineScope(CoroutineName("")).launch {
                        delay(1.seconds)
                        viewModel.enableButton(true)
                    }
                })
        }
    }
}

@Composable
fun UselessFactLanguageChooser(vm: UselessFactsViewModel, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
    ) {
        Text(
            text = vm.dropdownHeader,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clickable(onClick = {
                    vm.changeDropdownState()
                }, indication = null, interactionSource = MutableInteractionSource())
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_small))
                .align(Alignment.Center)
        )
        DropdownMenu(
            expanded = vm.isDropdownOpened,
            onDismissRequest = { vm.changeDropdownState() },
            modifier = Modifier.fillMaxWidth()
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = Language.English.name,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                onClick = {
                    vm.changeDropdownState(false)
                    vm.changeDropdownHeader(Language.English.name)
                    vm.changeCurrentLanguageId(Language.English.id)
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = Language.Deutsch.name,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                onClick = {
                    vm.changeDropdownState(false)
                    vm.changeDropdownHeader(Language.Deutsch.name)
                    vm.changeCurrentLanguageId(Language.Deutsch.id)
                }
            )
        }
    }
}

@Composable
fun UselessFactText(fact: String, modifier: Modifier = Modifier) {
    Text(
        text = fact,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        color = colorResource(id = R.color.light_grey),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Medium,
        modifier = modifier
    )
}

@Composable
fun UselessFactSource(source: String, modifier: Modifier = Modifier) {
    Text(
        text = source,
        fontSize = 20.sp,
        lineHeight = 20.sp,
        color = colorResource(id = R.color.light_grey),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Light,
        modifier = modifier
    )
}

@Composable
fun NextFactButton(vm: UselessFactsViewModel, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.black),
            contentColor = colorResource(id = R.color.light_grey)
        ),
        enabled = vm.isButtonEnabled,
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = stringResource(id = R.string.next_fact_button_text))
    }
}

@Preview(showBackground = true)
@Composable
fun UselessFactsAppPreview() {
    UselessFactsTheme(darkTheme = true) {
        UselessFacts()
    }
}