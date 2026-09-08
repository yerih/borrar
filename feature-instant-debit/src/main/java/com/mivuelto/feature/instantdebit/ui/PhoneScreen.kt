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
import androidx.compose.ui.unit.sp
import com.mivuelto.core.ui.SingleFormScreen
import com.mivuelto.core.ui.design.textfields.PhoneVisualTransformation
import com.mivuelto.feature.instantdebit.navigation.InstantDebitViewModel


@Composable
fun PhoneScreen(
    viewModel: InstantDebitViewModel,
    onBack: ()->Unit,
    onTaskDone: ()->Unit
){
    var phone by remember{ mutableStateOf("") }
    val phoneError = remember{ mutableStateOf(false) }
    val onDone: ()->Unit = {
        phoneError.value = phone.isBlank()
        if(!phoneError.value){
            viewModel.state.phone = phone
            onTaskDone()
        }
    }

    SingleFormScreen(
        title = "Ingrese teléfono",
        label = "Teléfono",
        onBack = onBack,
        isError = phoneError.value,
        fontSize = 37.sp,
        errorMsg = "El teléfono es requerido",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        visualTransformation = PhoneVisualTransformation(),
        keyboardActions = KeyboardActions(onDone = {onDone()}),
        onTaskDone = onDone,
        onValueChange = { phone = it }
    )
}
