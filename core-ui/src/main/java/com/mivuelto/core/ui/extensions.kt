package com.mivuelto.core.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Picture
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.DrawResult
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.mivuelto.core.ui.navigation.NavFeature
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import kotlin.coroutines.CoroutineContext


fun NavController.backToHome() = popBackStack(
    route = NavCommand.TypeSubRoute(
        NavFeature.HOME,
        NavFeature.HOME.route
    ).route,
    false
)

fun NavController.backTo(route: String) = popBackStack(route, false)

fun Modifier.isInvisible(value: Boolean) = alpha(if(value) 1f else 0f)
fun Modifier.isVisible(value: Boolean) = alpha(if(value) 1f else 0f)

fun String.ifThenDo(condition: Boolean, block: (String)->String): String = if(condition) block(this) else this

fun String.toNumber(): String = if(isEmpty()) "" else NumberFormat.getNumberInstance(Locale.GERMAN).format(toLong())

fun String.toCurrencyFmt(): String  = if (isEmpty()) "Bs 0,00" else {
    val out = NumberFormat.getNumberInstance(Locale.GERMAN).apply {
        minimumFractionDigits = 2
    }.format(toDouble()/100)
    "Bs $out"
}

fun String.toCurrency(): String = this.toCurrencyFmt().replace("Bs ", "")


fun String.mask(): String{
    if(length < 6) return this
    val toMaskLength = length/2
    var str = ""
    (0 .. toMaskLength).forEach { _ -> str += "*" }
    return replaceRange(startIndex = toMaskLength, endIndex = toMaskLength+toMaskLength/2, str)
}

fun String.args(vararg values: Any?)
= "${this}${values.map{ if(it is String && it.isEmpty()) "\"\"" else it}.joinToString ("/")}"


fun NavBackStackEntry.getBoolean(arg: NavArgs): Boolean = arguments?.getBoolean(arg.key)?:false
fun NavBackStackEntry.getString(arg: NavArgs): String = arguments?.getString(arg.key)?:""
fun NavBackStackEntry.getInt(arg: NavArgs): Int? = arguments?.getInt(arg.key)
fun NavBackStackEntry.getDouble(arg: NavArgs): Double = arguments?.getDouble(arg.key)?:-1.0

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController,
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry  = remember(this){ navController.getBackStackEntry(navGraphRoute) }
    return hiltViewModel(parentEntry)
}


fun Modifier.clickableNoRipple(
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    onClick: () -> Unit
) = composed(
    factory = {
        this.then(
            Modifier.clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onClick() }
            )
        )
    }
)

fun Context.getVersionName(): String = packageManager.getPackageInfo(packageName, 0).versionName
@RequiresApi(Build.VERSION_CODES.P)
fun Context.getVersionCode() = packageManager.getPackageInfo(packageName, 0).longVersionCode

fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
fun Context.toast(msgId: Int) = toast(getString(msgId))

fun launch(task: suspend ()->Unit) = CoroutineScope(Dispatchers.IO).launch { task() }
fun launch(dispatcher: CoroutineDispatcher, task: suspend ()->Unit) = CoroutineScope(dispatcher).launch{task()}


fun CacheDrawScope.catchComposable(picture: Picture, onFinished: ((Bitmap)->Unit)? = null): DrawResult {
    val width = this.size.width.toInt()
    val height = this.size.height.toInt()
    return onDrawWithContent {
        val pictureCanvas = Canvas(picture.beginRecording(
            width,
            height
        ))
        draw(this, this.layoutDirection, pictureCanvas, this.size) {
            this@onDrawWithContent.drawContent()
        }
        picture.endRecording()
        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawPicture(picture)
        }
        onFinished?.invoke(pictureToBitmap(picture))
    }
}

fun Modifier.catchComposable(picture: Picture): Modifier = drawWithCache { catchComposable(picture) }

fun pictureToBitmap(picture: Picture): Bitmap {
    val bitmap = Bitmap.createBitmap(
        picture.width,
        picture.height,
        Bitmap.Config.ARGB_8888
    )
    val canvas = android.graphics.Canvas(bitmap)
    canvas.drawColor(Color.WHITE)
    canvas.drawPicture(picture)
    return bitmap
}

fun NavHostController.navigatePopUp(
    route: String,  // route name where you want to navigate
    from: String // route you want from popUpTo.
) {
    this.navigate(route) {
        popUpTo(from) {
            inclusive = true // It can be changed to false if you
            // want to keep your fromRoute exclusive
        }
    }
}

fun ViewModel.launch(
    context: CoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
){
    viewModelScope.launch(context, start, block)
}

