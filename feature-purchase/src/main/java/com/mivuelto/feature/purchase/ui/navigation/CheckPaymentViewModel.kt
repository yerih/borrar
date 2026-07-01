package com.mivuelto.feature.purchase.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mediosdepago.corpocredit.core.ui_atomics.UiEvent
import com.mivuelto.core.domain.model.CheckPaymentModel
import com.mivuelto.core.ui.launch
import com.mivuelto.feature.purchase.ui.invoices.InvoiceModel
import com.mivuelto.feature.purchase.ui.login.LoginEffect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class CheckPaymentViewModel @Inject constructor(

): ViewModel(){

    var state by mutableStateOf(CheckPaymentModel())
        private set


    private val _effect = Channel<UiEvent>()
    val effect = _effect.receiveAsFlow()


    fun updateState(model: CheckPaymentModel){state = model}
    fun getInvoice(): InvoiceModel = InvoiceModel(
        ref = state.reference?:"empty",
        date = "22/06/2026 6:33 pm",
        bank = state.bank?:"empty",
        amount = state.amount?:"empty",
//        phone = state.phone?:"empty"
    )

    fun sendPayment(){
        launch {
            delay(3000)
            _effect.send(UiEvent.OnSuccess)
            delay(2000)
            _effect.send(UiEvent.TaskDone())
        }
    }
}