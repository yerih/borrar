package com.mivuelto.feature.purchase.ui.check_payment_data

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import com.mivuelto.core.domain.model.CheckPaymentModel
import com.mivuelto.core.ui.SingleFormScreen
import com.mivuelto.feature.purchase.ui.navigation.CheckPaymentViewModel

@Composable
fun ReferenceScreen(
    viewModel: CheckPaymentViewModel,
    onBack: ()->Unit = {},
    onTaskDone: ()->Unit = {}
){
    var reference by remember{ mutableStateOf("") }
    val referenceError = remember{ mutableStateOf(false) }
    val onDone: ()->Unit = {
        referenceError.value = reference.isBlank()
        if (!referenceError.value) {
            viewModel.state.reference = reference
            onTaskDone()
        }
    }

    SingleFormScreen(
        title = "Ingrese referencia",
        label = "Referencia",
        onBack = onBack,
        isError = referenceError.value,
        errorMsg = "La referencia es requerida",
        onValueChange = { reference = it },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {onDone()}),
        onTaskDone = onDone
    )
}