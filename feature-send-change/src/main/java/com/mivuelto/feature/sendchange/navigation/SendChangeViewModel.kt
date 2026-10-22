package com.mivuelto.feature.sendchange.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mediosdepago.corpocredit.core.ui_atomics.UiEvent
import com.mivuelto.core.domain.model.BankModel
import com.mivuelto.core.domain.model.InstantDebitModel
import com.mivuelto.core.domain.model.SendChangeCommand
import com.mivuelto.core.domain.usecase.ObserveBanksUseCase
import com.mivuelto.core.domain.usecase.ObserveUserUseCase
import com.mivuelto.core.domain.usecase.SendChangeUseCase
import com.mivuelto.core.domain.usecase.SyncBanksUseCase
import com.mivuelto.core.log
import com.mivuelto.core.toDateTimeFormatted
import com.mivuelto.core.ui.launch
import com.mivuelto.feature.sendchange.ui.invoices.InvoiceModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SendChangeViewModel @Inject constructor(
    private val observeUser: ObserveUserUseCase,
    private val observeBanks: ObserveBanksUseCase,
    private val syncBanks: SyncBanksUseCase,
    private val sendChangeUseCase: SendChangeUseCase,
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
        ref = "SC-${System.currentTimeMillis() % 10000}",
        date = LocalDateTime.now().toDateTimeFormatted(),
        time = "",
        bank = state.bank ?: BankModel(),
        businessBankRif = merchantRif ?: "J-12345678",
        amount = state.amount ?: "empty",
        phone = state.phone ?: "empty"
    )

    fun sendPayment() {
        launch {
            sendChangeUseCase(state.toSendChangeCommand())
                .fold(
                    onFailure = { e ->
                        log("excep = $e")
                        errorMsg = e.message ?: "Unknown error"
                        _effect.send(UiEvent.Error(msg = errorMsg))
                    },
                    onSuccess = {
                        log("onSuccess: result = $it")
                        _effect.send(UiEvent.OnSuccess)
                        delay(SUCCESS_VISIBLE_MS)
                        _effect.send(UiEvent.TaskDone())
                    }
                )
        }
    }

    private fun InstantDebitModel.toSendChangeCommand() = SendChangeCommand(
        amount = parseAmount(amount),
        phone = phone?.filter(Char::isDigit).orEmpty(),
        bankId = bank?.uuid.orEmpty(),
        document = idNumber.orEmpty()
    )

    private companion object {
        const val SUCCESS_VISIBLE_MS = 1_500L

        fun parseAmount(raw: String?): BigDecimal {
            val digits = raw?.filter(Char::isDigit).orEmpty()
            if (digits.isBlank()) return BigDecimal.ZERO
            return BigDecimal(digits).movePointLeft(2)
        }
    }
}
