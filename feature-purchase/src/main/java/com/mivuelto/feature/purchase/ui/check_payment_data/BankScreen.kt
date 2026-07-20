package com.mivuelto.feature.purchase.ui.check_payment_data

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mivuelto.core.domain.model.BankModel
import com.mivuelto.core.ui.BaseScreen
import com.mivuelto.core.ui.R
import com.mivuelto.core.ui.design.buttons.ButtonBorder
import com.mivuelto.core.ui.design.buttons.ButtonFilled
import com.mivuelto.core.ui.design.logos.CorpoCreditLogo
import com.mivuelto.core.ui.design.selectors.BankSelector
import com.mivuelto.core.ui.theme.Lato
import com.mivuelto.feature.purchase.ui.navigation.CheckPaymentViewModel





@Composable
fun BankScreen(
    viewModel: CheckPaymentViewModel,
    onBack: ()->Unit,
    onTaskDone: ()->Unit
){

    val banks = listOf(
        BankModel(R.drawable.logo_librepago, "0105", "Mercantil"),
        BankModel(R.drawable.logo_corpocredit, "0108", "Provincial BBVA"),
        BankModel(R.drawable.logo_librepago, "0105", "Mercantil"),
        BankModel(R.drawable.logo_corpocredit, "0108", "Provincial BBVA"),
        BankModel(R.drawable.logo_librepago, "0105", "Mercantil"),
        BankModel(R.drawable.logo_corpocredit, "0108", "Provincial BBVA"),
        BankModel(R.drawable.logo_librepago, "0105", "Mercantil"),
        BankModel(R.drawable.logo_corpocredit, "0108", "Provincial BBVA")
//    Bank(R.drawable.ic_bbva, "BBVA", "Cuenta terminación 9012"),
//    Bank(R.drawable.ic_santander, "Santander", "Cuenta terminación 3456")
    )
    val bank = remember{ mutableStateOf(banks[0]) }
    val bankError = remember{ mutableStateOf(false) }

    val onDone: ()->Unit = {
        bankError.value = bank.value.logo == null
        if(!bankError.value){
            viewModel.state.bank = bank.value
            onTaskDone()
        }
    }

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
                Text(text = "Ingrese banco", style = Lato.headlineMedium)
                Spacer(modifier = Modifier.height(15.dp))
            }

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                BankSelector(
                    modifier = Modifier,
                    onItemSelected = { index -> bank.value = banks[index] },
                    banks,
                )
            }


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
                        onDone()
                        onTaskDone()
                    }
                )

            }
        }

    }
}
