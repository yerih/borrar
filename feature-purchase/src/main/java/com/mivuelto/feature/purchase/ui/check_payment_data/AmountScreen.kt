package com.mivuelto.feature.purchase.ui.check_payment_data

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.mivuelto.core.ui.SingleFormScreen
import com.mivuelto.core.ui.design.textfields.DecimalCurrencyVisualTransformation
import com.mivuelto.feature.purchase.ui.navigation.CheckPaymentViewModel

@Composable
fun AmountScreen(
    viewModel: CheckPaymentViewModel,
    onBack: ()->Unit,
    onTaskDone: ()->Unit
){
    var amount by remember{ mutableStateOf("0") }
    val amountError = remember{ mutableStateOf(false) }
    val onDone: ()->Unit = {
        amountError.value = amount.isBlank()
        if(!amountError.value){
            viewModel.state.amount = amount
            onTaskDone()
        }
    }

    SingleFormScreen(
        title = "Ingrese monto",
        label = "Monto",
        initialValue = amount,
        errorMsg = "El monto es requerido",
        fontSize = 44.sp,
        isError = amountError.value,
        onBack = onBack,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {onDone()}),
        visualTransformation = DecimalCurrencyVisualTransformation("Bs. "),
        onTaskDone = onDone,
        onValueChange = { amount = it }
    )
}