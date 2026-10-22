package com.mivuelto.feature.instantdebit.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mediosdepago.corpocredit.core.ui_atomics.UiEvent
import com.mivuelto.core.domain.model.BankModel
import com.mivuelto.core.domain.model.InstantDebitModel
import com.mivuelto.core.domain.usecase.ObserveBanksUseCase
import com.mivuelto.core.domain.usecase.ObserveUserUseCase
import com.mivuelto.core.domain.usecase.SyncBanksUseCase
import com.mivuelto.core.toDateTimeFormatted
import com.mivuelto.core.ui.launch
import com.mivuelto.feature.instantdebit.ui.invoices.InvoiceModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class InstantDebitViewModel @Inject constructor(
    private val observeUser: ObserveUserUseCase,
    private val observeBanks: ObserveBanksUseCase,
    private val syncBanks: SyncBanksUseCase,
) : ViewModel() {

    var state by mutableStateOf(InstantDebitModel())
        private set

    var errorMsg = ""

    var merchantRif by mutableStateOf<String?>(null)
        private set

    var banks by mutableStateOf<List<BankModel>>(emptyList())
        private set

    private val _effect = Channel<UiEvent>()
    val effect = _effect.receiveAsFlow()

    init {
        launch {
            observeUser().collect { merchantRif = it?.merchantRif }
        }
    }

    fun updateState(model: InstantDebitModel) { state = model }

    private var banksLoaded = false

    fun loadBanks() {
        if (banksLoaded) return
        banksLoaded = true
        launch {
            observeBanks().collect { bankList ->
                banks = bankList
                if (bankList.isEmpty()) {
                    syncBanks().fold(
                        onSuccess = { banks = it },
                        onFailure = { e ->
                            errorMsg = e.message ?: "Unknown error"
                            _effect.send(UiEvent.Error(msg = errorMsg))
                        }
                    )
                }
            }
        }
    }

    fun getInvoice(): InvoiceModel = InvoiceModel(
        ref = "ID-${System.currentTimeMillis() % 10000}",
        date = LocalDateTime.now().toDateTimeFormatted(),
        time = "",
        bank = state.bank ?: BankModel(),
        businessBankRif = merchantRif ?: "J-12345678",
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