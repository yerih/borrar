package com.mivuelto.core.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
fun BaseScreen(
    onBack: () -> Unit = {},
    content: @Composable () -> Unit
){
    BackHandler(onBack = onBack)
    content()
}

