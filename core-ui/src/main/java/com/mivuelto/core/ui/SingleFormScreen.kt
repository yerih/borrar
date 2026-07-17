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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.mivuelto.core.ui.design.buttons.ButtonBorder
import com.mivuelto.core.ui.design.buttons.ButtonFilled
import com.mivuelto.core.ui.design.logos.CorpoCreditLogo
import com.mivuelto.core.ui.design.textfields.TextFieldCustom
import com.mivuelto.core.ui.theme.Lato


@Composable
fun SingleFormScreen(
    title: String,
    label: String,
    onBack: ()->Unit,
    onValueChange: (String)->Unit,
    onTaskDone: ()->Unit = {}
){
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
                value = "Text value",//reference.value,
                modifier = Modifier.padding(horizontal = 20.dp).align(Alignment.Center),
                onValueChange = onValueChange,//{ reference.value = it },
                label = label,
                isError = false,//referenceError.value,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onTaskDone() }),
                errorMessage = "La referencia es requerida"
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
                    onClick = onTaskDone
                )

            }
        }

    }
}



