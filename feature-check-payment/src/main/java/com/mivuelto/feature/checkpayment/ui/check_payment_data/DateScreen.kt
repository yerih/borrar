package com.mivuelto.feature.checkpayment.ui.check_payment_data

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mivuelto.core.toApiDate
import com.mivuelto.core.toDayMonthLabel
import com.mivuelto.core.toWeekdayMonthLabel
import com.mivuelto.core.ui.BaseScreen
import com.mivuelto.core.ui.R
import com.mivuelto.core.ui.design.HeaderTitles
import com.mivuelto.core.ui.design.buttons.ButtonBorder
import com.mivuelto.core.ui.design.buttons.ButtonFilled
import com.mivuelto.core.ui.design.logos.CorpoCreditLogo
import com.mivuelto.core.ui.design.textfields.TextFieldCustom
import com.mivuelto.feature.checkpayment.ui.navigation.CheckPaymentViewModel
import java.time.LocalDate

@Composable
fun DateScreen(
    viewModel: CheckPaymentViewModel,
    flowTitle: String,
    onBack: () -> Unit,
    onTaskDone: () -> Unit
) {
    var showCustomInput by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<DateOption?>(null) }
    val optionError = remember { mutableStateOf(false) }

    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    val monthFocus = remember { FocusRequester() }
    val yearFocus = remember { FocusRequester() }
    val dateError = remember { mutableStateOf(false) }

    val today = remember { LocalDate.now() }
    val yesterday = remember { today.minusDays(1) }
    val beforeYesterday = remember { today.minusDays(2) }

    val onPresetDone: () -> Unit = {
        val chosen = when (selectedOption) {
            DateOption.TODAY -> today
            DateOption.YESTERDAY -> yesterday
            DateOption.BEFORE_YESTERDAY -> beforeYesterday
            else -> null
        }
        optionError.value = chosen == null
        if (chosen != null) {
            viewModel.state.date = chosen.toApiDate()
            onTaskDone()
        }
    }

    val onCustomDone: () -> Unit = {
        val valid = day.isNotBlank() && month.isNotBlank() && year.isNotBlank()
        dateError.value = !valid
        if (valid) {
            viewModel.state.date =
                "${year.padStart(4, '0')}-${month.padStart(2, '0')}-${day.padStart(2, '0')}"
            onTaskDone()
        }
    }

    BaseScreen(onBack = onBack) {
        Box(modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp)) {
            Column(
                modifier = Modifier.align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CorpoCreditLogo(modifier = Modifier.padding(top = 10.dp))
                HeaderTitles(
                    flowTitle = flowTitle,
                    instruction = if (showCustomInput) "Ingrese fecha" else "Seleccione fecha"
                )
            }

            if (showCustomInput) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        DateField(
                            value = day,
                            label = "Día",
                            limit = 2,
                            isError = dateError.value && day.isBlank(),
                            imeAction = ImeAction.Next,
                            modifier = Modifier.weight(1f),
                            onValueChange = {
                                day = it
                                if (it.length == 2) monthFocus.requestFocus()
                            }
                        )
                        DateSeparator()
                        DateField(
                            value = month,
                            label = "Mes",
                            limit = 2,
                            isError = dateError.value && month.isBlank(),
                            imeAction = ImeAction.Next,
                            modifier = Modifier.weight(1f).focusRequester(monthFocus),
                            onValueChange = {
                                month = it
                                if (it.length == 2) yearFocus.requestFocus()
                            }
                        )
                        DateSeparator()
                        DateField(
                            value = year,
                            label = "Año",
                            limit = 4,
                            isError = dateError.value && year.isBlank(),
                            imeAction = ImeAction.Done,
                            modifier = Modifier.weight(1f).focusRequester(yearFocus),
                            keyboardActions = KeyboardActions(onDone = { onCustomDone() }),
                            onValueChange = { year = it }
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    DateOptionRow(
                        label = "Hoy, ${today.toDayMonthLabel()}",
                        selected = selectedOption == DateOption.TODAY,
                        onSelect = {
                            selectedOption = DateOption.TODAY
                            optionError.value = false
                        }
                    )
                    DateOptionRow(
                        label = "Ayer, ${yesterday.toDayMonthLabel()}",
                        selected = selectedOption == DateOption.YESTERDAY,
                        onSelect = {
                            selectedOption = DateOption.YESTERDAY
                            optionError.value = false
                        }
                    )
                    DateOptionRow(
                        label = beforeYesterday.toWeekdayMonthLabel(),
                        selected = selectedOption == DateOption.BEFORE_YESTERDAY,
                        onSelect = {
                            selectedOption = DateOption.BEFORE_YESTERDAY
                            optionError.value = false
                        }
                    )
                    DateOptionRow(
                        label = "Otra",
                        selected = selectedOption == DateOption.OTHER,
                        onSelect = {
                            selectedOption = DateOption.OTHER
                            showCustomInput = true
                        }
                    )

                    if (optionError.value) {
                        Text(
                            text = "Seleccione una opción",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 48.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                if (showCustomInput) {
                    ButtonBorder(
                        modifier = Modifier.weight(1f),
                        paddingHz = 0.dp,
                        text = stringResource(R.string.back),
                        onClick = { showCustomInput = false }
                    )
                } else {
                    ButtonBorder(
                        modifier = Modifier.weight(1f),
                        paddingHz = 0.dp,
                        text = stringResource(R.string.cancel),
                        onClick = onBack
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                ButtonFilled(
                    modifier = Modifier.weight(1f),
                    paddingHz = 0.dp,
                    text = stringResource(R.string.next),
                    onClick = {
                        if (showCustomInput) onCustomDone() else onPresetDone()
                    }
                )
            }
        }
    }
}

private enum class DateOption { TODAY, YESTERDAY, BEFORE_YESTERDAY, OTHER }

@Composable
private fun DateOptionRow(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onSelect)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun DateField(
    value: String,
    label: String,
    limit: Int,
    isError: Boolean,
    imeAction: ImeAction,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit
) {
    TextFieldCustom(
        value = value,
        onValueChange = { input ->
            val digits = input.filter { e -> e.isDigit() }
            if (digits.length <= limit) onValueChange(digits)
        },
        label = label,
        isError = isError,
        errorMessage = "Requerido",
        textStyle = LocalTextStyle.current.copy(fontSize = 24.sp),
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = imeAction),
        keyboardActions = keyboardActions
    )
}

@Composable
private fun DateSeparator() {
    Text(
        text = "/",
        fontSize = 24.sp,
        modifier = Modifier.padding(horizontal = 6.dp)
    )
}