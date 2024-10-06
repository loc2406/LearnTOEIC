package com.locnguyen.toeicexercises.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.locnguyen.toeicexercises.R

class GlobalHelper(private val context: Context) {

    val density = context.resources.displayMetrics.density
    val tinosBold by lazy { ResourcesCompat.getFont(context, R.font.tinos_bold) }
    val tinosItalic by lazy { ResourcesCompat.getFont(context, R.font.tinos_italic) }

    val idAdsApp = "ca-app-pub-9937095376409239~8917419808"
    val idAdsBanner = "ca-app-pub-9937095376409239/9347820758"
    val idAdsInterval = "ca-app-pub-9937095376409239/8967151572"
    val idAdsBannerTest = "ca-app-pub-3940256099942544/9214589741"
    val idAdsIntervalTest = "ca-app-pub-3940256099942544/1033173712"
}

fun Context.isHasNetWork(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Android 6 and later
        val network: Network? = connectivityManager?.activeNetwork

        return network?.let {
            val capabilities = connectivityManager.getNetworkCapabilities(it)
            capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        } ?: false
    } else {
        val networkInfo = connectivityManager?.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}

fun Context.loadImg(img: String, view: ImageView, listener: RequestListener<Drawable>? = null) {
    Glide.with(this)
        .load(img)
        .addListener(listener)
        .into(view)
}

fun Context.loadImg(img: Drawable, view: ImageView, listener: RequestListener<Drawable>? = null) {
    Glide.with(this)
        .load(img)
        .addListener(listener)
        .into(view)
}

fun Context.toastMessage(@StringRes stringRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, stringRes, duration).show()
}

fun Context.toastMessage(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Int.dpToPx(context: Context): Int {
    return (this * GlobalHelper(context).density).toInt()
}

fun Float.pxToDp(context: Context): Float {
    return (this / GlobalHelper(context).density)
}

