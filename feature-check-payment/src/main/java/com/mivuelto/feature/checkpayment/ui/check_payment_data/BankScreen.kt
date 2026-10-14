package com.mivuelto.feature.checkpayment.ui.check_payment_data

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mivuelto.core.domain.model.BankModel
import com.mivuelto.core.ui.BaseScreen
import com.mivuelto.core.ui.design.HeaderTitles
import com.mivuelto.core.ui.design.buttons.ButtonBorder
import com.mivuelto.core.ui.design.buttons.ButtonFilled
import com.mivuelto.core.ui.design.logos.CorpoCreditLogo
import com.mivuelto.core.ui.design.selectors.BankSelector

import com.mivuelto.core.ui.R.drawable.*
import com.mivuelto.feature.checkpayment.R
import com.mivuelto.feature.checkpayment.ui.navigation.CheckPaymentViewModel





@Composable
fun BankScreen(
    viewModel: CheckPaymentViewModel,
    flowTitle: String,
    onBack: ()->Unit,
    onTaskDone: ()->Unit
){

    val banks = remember(viewModel.banks) {
        viewModel.banks.map { it.copy(logo = ic_bank) }
    }
    val bank = remember { mutableStateOf<BankModel?>(null) }
    val bankError = remember { mutableStateOf(false) }
    val selectedBank = bank.value ?: banks.firstOrNull()

    LaunchedEffect(Unit) {
        viewModel.loadBanks()
    }

    val onDone: ()->Unit = {
        bankError.value = selectedBank == null
        if(!bankError.value){
            viewModel.state.bank = selectedBank
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

                HeaderTitles(
                    flowTitle = flowTitle,
                    instruction = stringResource(R.string.enter_bank)
                )


                Column(
                    modifier = Modifier.padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    if (banks.isNotEmpty()) {
                        BankSelector(
                            modifier = Modifier,
                            onItemSelected = { index -> bank.value = banks[index] },
                            banks = banks,
                        )
                    }
                }

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
                    onClick = { onDone() }
                )

            }
        }

    }
}
