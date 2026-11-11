package com.mivuelto.feature.instantdebit.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mediosdepago.corpocredit.core.ui_atomics.UiEvent
import com.mivuelto.core.domain.model.BankModel
import com.mivuelto.core.domain.model.DebinChargeCommand
import com.mivuelto.core.domain.model.DebinToken
import com.mivuelto.core.domain.model.DebinTokenCommand
import com.mivuelto.core.domain.model.InstantDebitModel
import com.mivuelto.core.domain.usecase.DebinChargeUseCase
import com.mivuelto.core.domain.usecase.DebinTokenUseCase
import com.mivuelto.core.domain.usecase.ObserveBanksUseCase
import com.mivuelto.core.domain.usecase.ObserveUserUseCase
import com.mivuelto.core.domain.usecase.SyncBanksUseCase
import com.mivuelto.core.log
import com.mivuelto.core.toDateFormatted
import com.mivuelto.core.toDateTimeFormatted
import com.mivuelto.core.toTimeFormatted
import com.mivuelto.core.ui.launch
import com.mivuelto.feature.instantdebit.ui.invoices.InvoiceModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class InstantDebitViewModel @Inject constructor(
    private val observeUser: ObserveUserUseCase,
    private val observeBanks: ObserveBanksUseCase,
    private val syncBanks: SyncBanksUseCase,
    private val debinTokenUseCase: DebinTokenUseCase,
    private val debinChargeUseCase: DebinChargeUseCase,
) : ViewModel() {

    var state by mutableStateOf(InstantDebitModel())
        private set

    var errorMsg = ""

    var tokenResult by mutableStateOf<DebinToken?>(null)
        private set

    var isCharging by mutableStateOf(false)
        private set

    var isRequestingToken by mutableStateOf(false)
        private set

    var tokenErrorMsg by mutableStateOf<String?>(null)
        private set

    var merchantRif by mutableStateOf<String?>(null)
        private set

    var merchantName: String? = null

    var banks by mutableStateOf<List<BankModel>>(emptyList())
        private set

    var referenceNumber: String? = null
    var time: String? = null
    var date: String? = null

    private val _effect = Channel<UiEvent>()
    val effect = _effect.receiveAsFlow()

    init {
        launch {
            observeUser().collect {
                merchantRif = it?.merchantRif
                merchantName = it?.merchantName
            }
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
        ref = referenceNumber ?: "empty",
        date = date ?: LocalDateTime.now().toDateTimeFormatted(),
        time = time ?: LocalDateTime.now().toTimeFormatted(),
        bank = state.bank ?: BankModel(),
        businessBankRif = merchantRif ?: "J-12345678",
        businessName = merchantName ?: "",
        amount = state.amount ?: "empty",
        phone = state.phone ?: "empty"
    )

    fun sendPayment() {
        if (isRequestingToken) return
        isRequestingToken = true
        launch {
            requestNewToken()
                .fold(
                    onFailure = { e ->
                        isRequestingToken = false
                        log("excep = $e")
                        errorMsg = e.message ?: "Unknown error"
                        _effect.send(UiEvent.Error(msg = errorMsg))
                    },
                    onSuccess = { token ->
                        isRequestingToken = false
                        log("onSuccess: token = ${token.id}")
                        tokenResult = token
                        _effect.send(UiEvent.OnSuccess)
                    }
                )
        }
    }

    /**
     * Re-solicita `POST /transactions/debin/token` cuando el token vigente
     * expiró (pantalla TOKEN). Al recibir el nuevo, [tokenResult] cambia y la
     * pantalla reinicia la cuenta regresiva con el `expiresInSeconds` nuevo.
     */
    fun retryToken() {
        if (isRequestingToken) return
        isRequestingToken = true
        tokenErrorMsg = null
        launch {
            requestNewToken()
                .fold(
                    onFailure = { e ->
                        isRequestingToken = false
                        tokenErrorMsg = e.message ?: "Unknown error"
                        log("token retry failed = $e")
                    },
                    onSuccess = { token ->
                        isRequestingToken = false
                        tokenResult = token
                    }
                )
        }
    }

    private suspend fun requestNewToken(): Result<DebinToken> =
        debinTokenUseCase(state.toDebinTokenCommand())

    private fun InstantDebitModel.toDebinTokenCommand() = DebinTokenCommand(
        amount = parseAmount(amount),
        phone = phone?.filter(Char::isDigit).orEmpty(),
        bankId = bank?.uuid.orEmpty(),
        document = idNumber.orEmpty()
    )

    /** Código de verificación guardado en [startCharge]; lo consume el LOADER de carga. */
    private var chargeTokenNumber: String? = null

    /** Guarda el código del usuario y prepara el charge para el LOADER de carga. */
    fun startCharge(token: String) {
        chargeTokenNumber = token
    }

    /** Paso 2 DEBIN: confirma con el `id` del token reservado + el código guardado. */
    fun chargePayment() {
        val id = tokenResult?.id
        val tokenNumber = chargeTokenNumber
        if (id.isNullOrBlank() || tokenNumber.isNullOrBlank()) {
            errorMsg = "Token operation not available"
            launch { _effect.send(UiEvent.Error(msg = errorMsg)) }
            return
        }
        isCharging = true
        launch {
            debinChargeUseCase(DebinChargeCommand(id = id, token = tokenNumber))
                .fold(
                    onFailure = { e ->
                        isCharging = false
                        log("excep = $e")
                        errorMsg = e.message ?: "Unknown error"
                        _effect.send(UiEvent.Error(msg = errorMsg))
                    },
                    onSuccess = { result ->
                        isCharging = false
                        referenceNumber = result.transaction.referenceNumber
                        date = result.transaction.created.toDateFormatted()
                        time = result.transaction.created.toTimeFormatted()
                        log("onSuccess: estado = ${result.estado}")
                        _effect.send(UiEvent.OnSuccess)
                        delay(SUCCESS_VISIBLE_MS)
                        _effect.send(UiEvent.TaskDone())
                    }
                )
        }
    }

    private companion object {
        const val SUCCESS_VISIBLE_MS = 1_500L

        fun parseAmount(raw: String?): BigDecimal {
            val digits = raw?.filter(Char::isDigit).orEmpty()
            if (digits.isBlank()) return BigDecimal.ZERO
            return BigDecimal(digits).movePointLeft(2)
        }
    }
}