package com.mivuelto.feature.purchase

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mivuelto.core.ui.BaseScreen
import com.mivuelto.core.ui.design.logos.CorpoCreditLogo


@Composable
fun HistoricalScreen(
    onBack: ()->Unit = {},
){

    BaseScreen(onBack){
        Box(modifier = Modifier.fillMaxSize()){
            CorpoCreditLogo(modifier = Modifier.align(Alignment.TopCenter))

            Text("Histórico", modifier = Modifier.align(Alignment.Center) )
        }
    }

}


