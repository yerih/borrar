package com.mivuelto.core.ui.design.dialogs

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.window.DialogProperties
import com.mivuelto.core.ui.R
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import com.mivuelto.core.ui.theme.Lato
import com.mivuelto.core.ui.design.buttons.ButtonBorder
import com.mivuelto.core.ui.design.buttons.ButtonFilled


@Composable
fun DialogConfirm(
    openDialog: MutableState<Boolean> = remember { mutableStateOf(true) },
    title: String = "",
    titleId: Int = R.string.confirm_action,
    descrp: String = "",
    descrpId: Int = R.string.confirm_action,
    textConfirm: String = "",
    textDism: String = "",
    textConfirmId: Int = R.string.yes,
    textDisId: Int = R.string.exit,
    onlyConfirm: Boolean = false,
    withoutButton: Boolean = false,
    onDismiss: ()->Unit = {},
    onConfirm: ()->Unit = {},
) {
    if (!openDialog.value) return
    Dialog(
        onDismissRequest = onDismiss,//{ openDialog.value = false },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        BackHandler(onBack = onDismiss)
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .padding(20.dp),
            shape = RoundedCornerShape(40.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 40.dp, horizontal = 20.dp)
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = descrp.ifEmpty { stringResource(id = titleId) },
                    style = Lato.dialogDescp,
                    modifier = Modifier.padding(vertical = 40.dp)
                )
                if(!withoutButton)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ){
                        if(onlyConfirm)
                            ButtonFilled(
                                textId = textConfirmId,//R.string.yes,
                                onClick = { onConfirm(); openDialog.value = false },
                                paddingHz = 30.dp
                            )
                        else {
                            ButtonBorder(
                                textId = textDisId,//R.string.exit,
                                modifier = Modifier.weight(1f),
                                paddingHz = 0.dp,
                                onClick = onDismiss
                            )
                            ButtonFilled(
                                textId = textConfirmId,//R.string.yes,
                                modifier = Modifier.weight(1f),
                                paddingHz = 0.dp,
                                onClick = { onConfirm(); openDialog.value = false }
                            )
                        }
                    }

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
private fun DefaultPreview() { CorpoCreditTheme { DialogConfirm() } }

@Preview(
    widthDp = 420,
    heightDp = 680,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun DefaultPreview2() { CorpoCreditTheme { DialogConfirm(onlyConfirm = true, textConfirmId = R.string.cancel) } }

