package com.mivuelto.feature.purchase.ui.check_payment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mivuelto.core.ui.BaseScreen
import com.mivuelto.core.ui.design.buttons.ButtonFilled
import com.mivuelto.core.ui.design.buttons.ButtonBorder
import com.mivuelto.core.ui.design.logos.CorpoCreditLogo
import com.mivuelto.core.ui.design.textfields.DecimalCurrencyVisualTransformation
import com.mivuelto.core.ui.design.textfields.OutlinedTextFieldCustom
import com.mivuelto.core.ui.theme.Lato
import com.mivuelto.feature.purchase.R

@Composable
fun CheckPaymentScreen(
    onTaskDone: (reference: String, amount: String) -> Unit = { _, _ -> },
    onBack: () -> Unit = {}
) {

    BaseScreen(onBack) {

        val reference = remember { mutableStateOf("") }
        val amount = remember { mutableStateOf("") }
        val phone = remember { mutableStateOf("") }
        val bank = remember { mutableStateOf("") }
        val referenceError = remember { mutableStateOf(false) }
        val amountError = remember { mutableStateOf(false) }


        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        ){
            item{

                Box{
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        CorpoCreditLogo(
                            modifier = Modifier.padding(top = 10.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = stringResource(R.string.verify_payment), style = Lato.headlineMedium)
                        Spacer(modifier = Modifier.height(15.dp))


                        OutlinedTextFieldCustom(
                            value = reference.value,
                            onValueChange = { reference.value = it },
                            label = stringResource(R.string.reference),
                            isError = referenceError.value,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            errorMessage = "La referencia es requerida",
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextFieldCustom(
                            value = amount.value,
                            onValueChange = { input ->
                                val digitsOnly = input.filter { it.isDigit() }
                                amount.value = digitsOnly.dropWhile { it == '0' }
//                                amount.value = it
                                            },
                            label = stringResource(R.string.amount),
                            isError = amountError.value,
                            errorMessage = "El monto es requerido",
                            isPassword = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                            visualTransformation = DecimalCurrencyVisualTransformation("Bs. "),
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextFieldCustom(
                            value = phone.value,
                            onValueChange = { phone.value = it },
                            label = stringResource(R.string.phone)+" (opcional)",
                            isPassword = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextFieldCustom(
                            value = bank.value,
                            onValueChange = { phone.value = it },
                            label = stringResource(R.string.bank)+" (opcional)",
                            isPassword = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ){
                            ButtonBorder(
                                modifier = Modifier.weight(1f),
                                paddingHz = 0.dp,
                                text = stringResource(com.mivuelto.core.ui.R.string.cancel),
                                onClick = onBack
                            )

                            Spacer(modifier = Modifier.width(10.dp))
                            ButtonFilled(
                                modifier = Modifier.weight(1f),
                                paddingHz = 0.dp,
                                text = "Verificar",
                                onClick = {

                                    referenceError.value = reference.value.isBlank()
                                    amountError.value = amount.value.isBlank()

                                    if (!referenceError.value && !amountError.value) {
                                        onTaskDone(reference.value, amount.value)
                                    }
                                }
                            )

                        }
                    }
                }

            }
        }
    }
}
