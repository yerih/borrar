package com.mivuelto.core.ui.design.snackbar

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mivuelto.core.ui.theme.RedErrorMsg


@Composable
fun SnackBarHost(
    hostState: SnackbarHostState,
){
    SnackbarHost(hostState = hostState){snackbarData ->
        Snackbar (
            snackbarData = snackbarData,
            containerColor = Color.White,
            contentColor = RedErrorMsg,
            actionColor = RedErrorMsg,
            actionContentColor = Color.Black,
        )
    }
}


suspend fun SnackbarHostState.show(
    msg: String,
    actionLabel: String = "Cerrar",
    duration: SnackbarDuration = SnackbarDuration.Short,
    action: () -> Unit = {}
){
    showSnackbar(
        message = msg,
        actionLabel = actionLabel,
        withDismissAction = true,
        duration = duration
    ).also{ result ->
        when(result){
            SnackbarResult.Dismissed -> Unit
            SnackbarResult.ActionPerformed -> action()
        }
    }
}




