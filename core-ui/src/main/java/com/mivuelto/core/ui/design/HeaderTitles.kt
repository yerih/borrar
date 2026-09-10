package com.mivuelto.core.ui.design

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mivuelto.core.ui.theme.Lato


@Composable
fun HeaderTitles(
    flowTitle: String = "Flow title",
    instruction: String = "Enter data instruction",
){

    Spacer(modifier = Modifier.height(10.dp))
    Text(text = flowTitle, style = Lato.headlineSmall)
    Spacer(modifier = Modifier.height(10.dp))
    Text(text = instruction, style = Lato.headlineMedium)
    Spacer(modifier = Modifier.height(15.dp))
}



