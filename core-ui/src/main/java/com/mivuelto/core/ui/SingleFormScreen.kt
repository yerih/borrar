package com.mivuelto.core.ui

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.mivuelto.core.ui.design.buttons.ButtonBorder
import com.mivuelto.core.ui.design.buttons.ButtonFilled
import com.mivuelto.core.ui.design.logos.CorpoCreditLogo
import com.mivuelto.core.ui.design.textfields.DecimalCurrencyVisualTransformation
import com.mivuelto.core.ui.design.textfields.TextFieldCustom
import com.mivuelto.core.ui.theme.Lato


@Composable
fun SingleFormScreen(
    title: String,
    label: String,
    errorMsg: String,
    onBack: ()->Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    onValueChange: (String)->Unit,
    onTaskDone: ()->Unit = {}
){
    val textValue = remember{ mutableStateOf("") }

    BaseScreen(onBack = onBack) {

        Box(modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp)){
            Column(
                modifier = Modifier.align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                CorpoCreditLogo(
                    modifier = Modifier.padding(top = 10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text(text = title, style = Lato.headlineMedium)
                Spacer(modifier = Modifier.height(15.dp))
            }


            TextFieldCustom(
                value = textValue.value,
                modifier = Modifier.padding(horizontal = 20.dp).align(Alignment.Center),
                onValueChange = {
                    textValue.value = it
                    onValueChange(it)
                },
                label = label,
                isError = isError,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                visualTransformation = visualTransformation,
                errorMessage = errorMsg
            )

            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
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
                    text = stringResource(R.string.next),
                    onClick = {
                        onTaskDone()
                    }
                )

            }
        }

    }
}



