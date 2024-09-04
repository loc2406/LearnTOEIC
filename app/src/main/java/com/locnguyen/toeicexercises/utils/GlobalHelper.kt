package com.locnguyen.toeicexercises.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Network
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.MyBottomSheetDialogBinding
import com.locnguyen.toeicexercises.fragment.MainFragment
import com.locnguyen.toeicexercises.model.Word

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