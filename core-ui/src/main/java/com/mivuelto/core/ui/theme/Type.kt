/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mivuelto.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

object Lato{

    private val fontFamily = FontFamily(
        Font(R.font.lato_black, FontWeight.Black, FontStyle.Normal),
        Font(R.font.lato_blackitalic, FontWeight.Black, FontStyle.Italic),
        Font(R.font.lato_bold, FontWeight.Bold, FontStyle.Normal),
        Font(R.font.lato_bolditalic, FontWeight.Bold, FontStyle.Normal),
        Font(R.font.lato_italic, FontWeight.Black, FontStyle.Italic),
        Font(R.font.lato_light, FontWeight.Light, FontStyle.Normal),
        Font(R.font.lato_lightitalic, FontWeight.Light, FontStyle.Italic),
        Font(R.font.lato_regular, FontWeight.Medium, FontStyle.Normal),
        Font(R.font.lato_thin, FontWeight.Thin, FontStyle.Normal),
        Font(R.font.lato_thinitalic, FontWeight.Thin, FontStyle.Italic),
    )

    private val textStyleDefault = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )

    val bodyLarge  = textStyleDefault.copy(fontSize = 20.sp)
    val bodyMedium = textStyleDefault.copy(fontSize = 16.sp)
    val bodySmall  = textStyleDefault.copy(fontSize = 14.sp)

    val labelLarge  = textStyleDefault.copy(fontSize = 20.sp)
    val labelMedium = textStyleDefault.copy(fontSize = 16.sp)
    val labelSmall  = textStyleDefault.copy(fontSize = 14.sp)

    val titleLarge  = textStyleDefault.copy(fontSize = 20.sp)
    val titleMedium = textStyleDefault.copy(fontSize = 16.sp)
    val titleSmall  = textStyleDefault.copy(fontSize = 14.sp)

    val displayLarge  = textStyleDefault.copy(fontSize = 20.sp)
    val displayMedium = textStyleDefault.copy(fontSize = 16.sp)
    val displaySmall  = textStyleDefault.copy(fontSize = 14.sp)

    val headlineLarge  = textStyleDefault.copy(fontSize = 28.sp)
    val headlineMedium = textStyleDefault.copy(fontSize = 24.sp)
    val headlineSmall  = textStyleDefault.copy(fontSize = 17.sp)

    val welcomeStyle = textStyleDefault.copy(fontSize = 28.sp)

    val buttonHome = textStyleDefault.copy(fontSize = 18.sp, lineHeight = 17.sp)

    val screenTitle       = textStyleDefault.copy(fontSize = 18.sp, color = BlueFonts,)
    val screenSubTitle    = textStyleDefault.copy(fontSize = 23.sp, color = BlueFonts,)
    val titleBlack        = screenSubTitle.copy(fontSize = 25.sp, color = Color.Black, textAlign = TextAlign.Center, fontWeight = FontWeight.W700)
    val subtitleBlack     = screenSubTitle.copy(fontSize = 25.sp, color = Color.Black, textAlign = TextAlign.Center, fontWeight = FontWeight.W600)

    val keyboardTextField = textStyleDefault.copy(fontSize = 45.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.W500, lineHeight = 1.sp)
    val textField = textStyleDefault.copy(fontSize = 24.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.W500)

    val amountAsTitle = textStyleDefault.copy(fontSize = 34.sp, fontWeight = FontWeight.W500, textAlign = TextAlign.Center)
    val instructionMsg = textStyleDefault.copy(fontSize = 24.sp, fontWeight = FontWeight.W700, color = BlueFonts, textAlign = TextAlign.Center)

    fun invoiceLabel(weightless: Boolean) = textStyleDefault.copy(fontSize = 18.sp, fontWeight = if(weightless) FontWeight.W500 else FontWeight.W700)
    val invoiceMerchant = textStyleDefault.copy(fontSize = 18.sp, fontWeight = FontWeight.W500, textAlign = TextAlign.Center)
    val invoiceTitle = textStyleDefault.copy(fontSize = 19.sp, fontWeight = FontWeight.W700, textAlign = TextAlign.Center)
    val invoiceIconTitle = textStyleDefault.copy(fontSize = 20.sp, fontWeight = FontWeight.W700, textAlign = TextAlign.Center)
    val invoiceAddress = textStyleDefault.copy(fontSize = 14.sp, fontWeight = FontWeight.W500, textAlign = TextAlign.Center)
    val detail = invoiceAddress.copy(textAlign = TextAlign.End)

    val screenMsg = headlineLarge.copy(color = BlueFonts)
    val errorMsg = headlineLarge.copy(color = RedErrorMsg, textAlign = TextAlign.Center, lineHeight = 30.sp)
    val errorTitle = textStyleDefault.copy(fontSize = 23.sp, color = Color.Black, fontWeight = FontWeight.W600, textAlign = TextAlign.Center)

    val dialogTitle = bodyLarge.copy(fontSize = 25.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.W700)
    val dialogDescp = dialogTitle.copy(fontSize = 25.sp, fontWeight = FontWeight.W800)

}

object Roboto{
    private val fontFamily = FontFamily(
        Font(R.font.roboto_black, FontWeight.Black, FontStyle.Normal),
        Font(R.font.roboto_blackitalic, FontWeight.Black, FontStyle.Italic),
        Font(R.font.roboto_bold, FontWeight.Bold, FontStyle.Normal),
        Font(R.font.roboto_bolditalic, FontWeight.Bold, FontStyle.Normal),
        Font(R.font.roboto_italic, FontWeight.Black, FontStyle.Italic),
        Font(R.font.roboto_light, FontWeight.Light, FontStyle.Normal),
        Font(R.font.roboto_lightitalic, FontWeight.Light, FontStyle.Italic),
        Font(R.font.roboto_regular, FontWeight.Medium, FontStyle.Normal),
        Font(R.font.roboto_thin, FontWeight.Thin, FontStyle.Normal),
        Font(R.font.roboto_thinitalic, FontWeight.Thin, FontStyle.Italic),
    )

    private val textStyleDefault = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    )
    val buttonKeyboard = textStyleDefault.copy(fontFamily = fontFamily, fontSize = 27.sp, fontWeight = FontWeight.W500)
}

val Typography = with(Lato){
    return@with Typography(
        bodyLarge = bodyLarge,
        bodyMedium = bodyMedium,
        bodySmall = bodySmall,

        labelLarge = labelLarge,
        labelMedium = labelMedium,
        labelSmall = labelSmall,

        titleLarge = titleLarge,
        titleMedium = titleMedium,
        titleSmall = titleSmall,

        displayLarge = displayLarge,
        displayMedium = displayMedium,
        displaySmall = displaySmall,

        headlineLarge = headlineLarge,
        headlineMedium = headlineMedium,
        headlineSmall = headlineSmall,
    )
}
