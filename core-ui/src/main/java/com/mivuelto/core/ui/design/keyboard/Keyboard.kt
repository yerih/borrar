@file:OptIn(ExperimentalComposeUiApi::class)

package com.mivuelto.core.ui.design.keyboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import com.mivuelto.core.ui.theme.Lato
import com.mivuelto.core.ui.theme.toCurrencyFmt
import com.mivuelto.core.ui.theme.toNumber
import com.mivuelto.core.ui.theme.ifThenDo
import com.mivuelto.core.ui.theme.toCurrency
import com.mivuelto.core.ui.design.buttons.ButtonKeyboard


@ExperimentalComposeUiApi
@Composable
fun Keyboard(
    toCompare: String? = null,
    isCurrency: Boolean = false,
    noDots: Boolean = false,
    noLimit: Boolean = false,
    onlyText: Boolean = false,
    largeTextPreview: Boolean = false,
    modifier: Modifier,
    onKeyClicked: (Int)->Unit = {},
    onEnterClicked: (granted: Boolean, text: String) -> Unit = { _, _ -> }
) {
    var text by remember { mutableStateOf("") }
    var textStyleField by remember { mutableStateOf(Lato.keyboardTextField) }
    var textToShow by remember { mutableStateOf(if(isCurrency)"Bs 0,00" else "") }

    val limit = when{
        toCompare != null -> 9
        isCurrency        -> 12
        noLimit           -> 99
        else              -> 9
    }

    LaunchedEffect(text) {
        if(isCurrency){
            textStyleField = if(text.length < 9) Lato.keyboardTextField
            else Lato.keyboardTextField.copy(fontSize = 30.sp)
        }
    }

    Column(modifier = modifier) {
        CompositionLocalProvider(
            LocalTextInputService provides null,
            LocalTextSelectionColors provides TextSelectionColors(
                handleColor = Color.Transparent,
                backgroundColor = Color.Transparent
            ),
        ) {
            when{
                toCompare != null -> BasicTextField(
                    value = textToShow,
                    textStyle = textStyleField,
                    singleLine = true,
                    cursorBrush = SolidColor(Color.Transparent),
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = toCompare?.let { return@let PasswordVisualTransformation() }?:VisualTransformation.None,
                    onValueChange = {}
                )
                else -> BasicTextField(
                    value = textToShow,
                    textStyle = textStyleField,
                    singleLine = true,
                    cursorBrush = SolidColor(Color.Transparent),
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {}
                )
            }
            Divider(thickness = 2.dp, modifier = Modifier.padding(bottom = 15.dp))
        }
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(3),//GridCells.Adaptive(90.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp),
        ) {
            items(12) { i ->
                ButtonKeyboard(
                    id = i + 1,
                    modifier = Modifier.fillMaxWidth().height(60.dp)
                ) { key ->
                    when{
                        key == 10               -> text = text.dropLast(1)
                        key == 12               -> {
                            text.ifEmpty { return@ButtonKeyboard }
                            text.isZero{ return@ButtonKeyboard }
                            return@ButtonKeyboard onEnterClicked(
                                checkValue(text, toCompare),
                                text.ifThenDo(isCurrency) { return@ifThenDo it.toCurrency() }
                            )
                        }
                        text.length == limit    -> {/*nothing*/}
                        key == 11               -> if(!text.isZero() || toCompare != null) text += "0"
                        key == 13               -> text = ""
                        else                    -> text += key
                    }
                    textToShow = when{
                        toCompare != null || onlyText -> text
                        isCurrency                    -> text.toCurrencyFmt()
                        noDots                        -> text.toNumber().replace(".", "")
                        else                          -> text.toNumber()
                    }
                    onKeyClicked(key)
                }
            }
        }
    }
}


private fun checkValue(value: String, comparator: String?): Boolean = comparator?.let { return value == comparator }?: false
private inline fun String.isZero(doThis: ()->Unit = {}): Boolean{
    if(isEmpty()) return false
    if(toLong() == 0L) doThis()
    return toLong() == 0L
}

@Preview(
    widthDp = 360,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun KeyboardPasswordPreview() {
    CorpoCreditTheme { Keyboard(toCompare = "1234", modifier = Modifier.fillMaxWidth()) }
}


@Preview(
    widthDp = 360,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun KeyboardNumberPreview() {
    CorpoCreditTheme { Keyboard(modifier = Modifier.fillMaxWidth()) }
}


@Preview(
    widthDp = 360,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun KeyboardAmountPreview() {
    CorpoCreditTheme { Keyboard(isCurrency = true, modifier = Modifier.fillMaxWidth()) }
}
