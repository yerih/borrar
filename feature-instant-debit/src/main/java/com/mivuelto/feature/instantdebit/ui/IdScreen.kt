package com.mivuelto.feature.instantdebit.ui

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.mivuelto.core.ui.SingleFormScreen
import com.mivuelto.feature.instantdebit.navigation.InstantDebitViewModel

@Composable
fun IdScreen(
    viewModel: InstantDebitViewModel,
    flowTitle: String,
    onBack: () -> Unit,
    onTaskDone: () -> Unit
) {
    var idNumber by remember { mutableStateOf("") }
    val idError = remember { mutableStateOf(false) }
    val onDone: () -> Unit = {
        idError.value = idNumber.isBlank()
        if (!idError.value) {
            viewModel.state.idNumber = idNumber
            onTaskDone()
        }
    }

    SingleFormScreen(
        flowTitle = flowTitle,
        title = "Ingrese cédula",
        label = "Cédula",
        onBack = onBack,
        isError = idError.value,
        errorMsg = "La cédula es requerida",
        onValueChange = { idNumber = it },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        digitsOnly = true,
        onTaskDone = onDone
    )
}
