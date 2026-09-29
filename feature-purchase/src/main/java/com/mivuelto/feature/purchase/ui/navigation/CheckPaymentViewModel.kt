package com.mivuelto.feature.purchase.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mediosdepago.corpocredit.core.ui_atomics.UiEvent
import com.mivuelto.core.domain.model.BankModel
import com.mivuelto.core.domain.model.CheckPaymentModel
import com.mivuelto.core.domain.repository.AuthRepository
import com.mivuelto.core.log
import com.mivuelto.core.ui.launch
import com.mivuelto.feature.purchase.ui.invoices.InvoiceModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject


@HiltViewModel
class CheckPaymentViewModel @Inject constructor(

    val authRepo: AuthRepository,
): ViewModel(){

    var state by mutableStateOf(CheckPaymentModel())
        private set

    var errorMsg = ""


    private val _effect = Channel<UiEvent>()
    val effect = _effect.receiveAsFlow()


    fun updateState(model: CheckPaymentModel){state = model}
    fun getInvoice(): InvoiceModel = InvoiceModel(
        ref = state.reference?:"empty",
        date = "22/06/2026 6:33 pm",
        bank = state.bank?: BankModel(),
        amount = state.amount?:"empty",
        phone = state.phone?:"empty"
    )

    fun sendPayment(){
        launch(Dispatchers.IO) {
            authRepo.login("abc", "password", terminalSerial = "123456")
                .fold(
                    onFailure = { e ->
                        log("excep = $e")
                        errorMsg = e.message.toString()
                        _effect.send(UiEvent.Error(msg = "${e.message}"))
                    },
                    onSuccess = {}
                )
//            delay(3000)
//            _effect.send(UiEvent.OnSuccess)
//            delay(2000)
//            _effect.send(UiEvent.TaskDone())
        }
    }
}