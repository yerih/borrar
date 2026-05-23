package com.mivuelto.feature.purchase

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mivuelto.core.ui.design.HeaderAndFooter2
import com.mivuelto.core.ui.design.buttons.ButtonHome
import com.mivuelto.core.ui.getVersionName
import com.mivuelto.core.ui.theme.Lato
import com.mivuelto.core.ui.R
import com.mivuelto.core.ui.theme.CorpoCreditTheme


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onBack: ()->Unit = {},
) {
    val context = LocalContext.current as Activity
    val version = LocalContext.current.getVersionName()


    HeaderAndFooter2(isScrollable = false) {
        ConstraintLayout(modifier = Modifier.fillMaxHeight()) {
            val (menu, versionRef) = createRefs()
            val spaced = 15.dp
            LazyVerticalGrid(
                modifier = Modifier.constrainAs(menu) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top)
                    bottom.linkTo(versionRef.top)
                },
                columns = object : GridCells {
                    override fun Density.calculateCrossAxisCellSizes(
                        availableSize: Int,
                        spacing: Int,
                    ): List<Int> {
                        val v = (availableSize - spacing) / 2
                        return listOf(v, v)
                    }

                },
                verticalArrangement = Arrangement.spacedBy(spaced),
                horizontalArrangement = Arrangement.spacedBy(spaced),
                contentPadding = PaddingValues(horizontal = spaced),
            ) {
                items(2) { i ->
                    ButtonHome(
                        textId = getTextId(i),
                        idIcon = getDrawableId(i),
                        modifier = Modifier.height(130.dp),
                    ) {

                    }
                }
            }
            Text(
                text = "Version $version",
                style = Lato.titleSmall,
                modifier = Modifier.constrainAs(versionRef){
                    centerHorizontallyTo(parent)
                    top.linkTo(menu.bottom, 10.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                }
            )
        }
    }
}


private fun getTextId(i: Int): Int {
    return when (i) {
        0 -> R.string.purchase
        1 -> R.string.anulacion
        2 -> R.string.last_transaction
        3 -> R.string.operations_report
        4 -> R.string.cierre
        5 -> R.string.otras_operaciones
        else -> R.string.enter
    }
}

private fun getDrawableId(i: Int): Int {
    return when (i) {
        0 -> R.drawable.ic_compra
        1 -> R.drawable.ic_anulacion
        2 -> R.drawable.ic_transacciones
        3 -> R.drawable.ic_resumen_operaciones
        4 -> R.drawable.ic_cierre
        5 -> R.drawable.ic_otras_operaciones
        else -> R.drawable.ic_resumen_operaciones
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    CorpoCreditTheme {
        HomeScreen(modifier = Modifier.padding(10.dp))
    }
}


