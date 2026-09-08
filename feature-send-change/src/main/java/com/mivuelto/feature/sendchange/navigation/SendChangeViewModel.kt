package com.mivuelto.feature.sendchange.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mediosdepago.corpocredit.core.ui_atomics.UiEvent
import com.mivuelto.core.domain.model.BankModel
import com.mivuelto.core.domain.model.InstantDebitModel
import com.mivuelto.core.ui.launch
import com.mivuelto.feature.sendchange.ui.invoices.InvoiceModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class SendChangeViewModel @Inject constructor(
) : ViewModel() {

    var state by mutableStateOf(InstantDebitModel())
        private set

    private val _effect = Channel<UiEvent>()
    val effect = _effect.receiveAsFlow()

    fun updateState(model: InstantDebitModel) { state = model }

    fun getInvoice(): InvoiceModel = InvoiceModel(
        ref = "SC-${System.currentTimeMillis() % 10000}",
        date = "22/06/2026 6:33 pm",
        bank = state.bank ?: BankModel(),
        amount = state.amount ?: "empty",
        phone = state.phone ?: "empty"
    )

    fun sendPayment() {
        launch {
            delay(3000)
            _effect.send(UiEvent.OnSuccess)
            delay(2000)
            _effect.send(UiEvent.TaskDone())
        }
    }
}
