package com.locnguyen.toeicexercises.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Network
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener

class GlobalHelper(private val context: Context) {

    val density = context.resources.displayMetrics.density

    fun loadImg(img: String, view: ImageView, listener: RequestListener<Drawable>? = null){
        Glide.with(context)
            .load(img)
            .addListener(listener)
            .into(view)
    }
}

fun Context.isHasNetWork(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network: Network? = cm.activeNetwork
    return (network != null)
}

fun Context.toastMessage(@StringRes stringRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, stringRes, duration).show()
}

fun Int.dpToPx(context: Context): Int {
    return (this * GlobalHelper(context).density).toInt()
}

fun Float.pxToDp(context: Context): Float {
    return (this / GlobalHelper(context).density)
}

