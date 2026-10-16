package com.mivuelto.core.ui.design.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mivuelto.core.ui.R
import com.mivuelto.core.ui.design.LottieAnim
import com.mivuelto.core.ui.design.buttons.ButtonFilled
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import com.mivuelto.core.ui.theme.Lato


@Composable
fun DialogError(
    openDialog: MutableState<Boolean> = remember { mutableStateOf(true) },
    title: String = "",
    titleId: Int? = R.string.alert,
    descrp: String = "",
    descrpId: Int = R.string.action_complete,
    textConfirm: String = "",
    textConfirmId: Int = R.string.accept,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    if (!openDialog.value) return
    Dialog(onDismissRequest = { openDialog.value = false; onDismiss() }) {
        Card(
            modifier = Modifier.padding(16.dp).wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(40.dp).wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = title.ifEmpty { titleId?.run{stringResource(id = titleId)} ?: "" },
                    style = Lato.dialogTitle
                )
                LottieAnim(
                    rawId = R.raw.failed,
                    iterations = 1,
                    modifier = Modifier.padding(top = 40.dp)
                )
                Text(
                    text = descrp.ifEmpty { stringResource(id = descrpId) },
                    style = Lato.dialogDescp,
                    modifier = Modifier.padding(bottom = 40.dp)
                )
                ButtonFilled(
                    text = textConfirm.takeIf { it.isNotEmpty() },
                    textId = textConfirmId,
                    modifier = Modifier.fillMaxWidth(),
                    paddingHz = 0.dp,
                    onClick = { onConfirm(); openDialog.value = false }
                )
            }
        }
    }
}


@Preview(
    widthDp = 420,
    heightDp = 680,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun DefaultPreview() {
    CorpoCreditTheme {
        DialogError()
    }
}
