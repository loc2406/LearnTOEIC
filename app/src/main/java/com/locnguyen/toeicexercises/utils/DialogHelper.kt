package com.locnguyen.toeicexercises.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.reflect.TypeToken
import com.google.mlkit.vision.text.Text.Line
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.model.Word
import com.locnguyen.toeicexercises.model.WordKindMean

class DialogHelper(private val context: Context) {

    companion object{
        private var loadingDialog: Dialog? = null

        fun getLoadingDialog(context: Context): Dialog = loadingDialog ?: let{
            loadingDialog = Dialog(context).apply {
                setContentView(R.layout.loading_dialog)
                window?.apply {
                    attributes?.gravity = Gravity.CENTER
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    setLayout(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT
                    )
                    setDimAmount(0.5f)
                    setCancelable(false)
                }
            }

            loadingDialog!!
        }
    }

    private val tinosRegular = ResourcesCompat.getFont(context, R.font.tinos_regular)
    private val tinosBold = ResourcesCompat.getFont(context, R.font.tinos_bold)
    private val tinosItalic = ResourcesCompat.getFont(context, R.font.tinos_italic)

    fun getFinishedExamDialog(
        positiveAction: (() -> Unit)?
    ): AlertDialog {
        return AlertDialog.Builder(context, R.style.MyAlertDialog).apply {
            val titleView = TextView(this@DialogHelper.context).apply {
                text = this@DialogHelper.context.getString(R.string.User_finished_exam_title)
                typeface = tinosBold
                setTextColor(this@DialogHelper.context.getColor(R.color.secondPrimary))
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setPadding(
                    15.dpToPx(context),
                    15.dpToPx(context),
                    15.dpToPx(context),
                    0
                )
                textSize = this@DialogHelper.context.resources.getDimension(R.dimen.big_title).pxToDp(context)
            }

            val contentView = TextView(this@DialogHelper.context).apply {
                text = this@DialogHelper.context.getString(R.string.User_finished_exam_content)
                typeface = tinosRegular
                setTextColor(this@DialogHelper.context.getColor(R.color.black))
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setPadding(
                    15.dpToPx(context),
                    15.dpToPx(context),
                    15.dpToPx(context),
                    0
                )
            }

            setCustomTitle(titleView)
            setView(contentView)
            setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Nộp bài") { dialog, _ ->
                positiveAction?.invoke()
                dialog.dismiss()
            }
        }.create()
    }

    fun getExamInstructionDialog(content: String, positiveAction: (() -> Unit)?): AlertDialog {
        return AlertDialog.Builder(context, R.style.MyAlertDialog).apply {
            val titleView = TextView(this@DialogHelper.context).apply {
                text = this@DialogHelper.context.getString(R.string.Exam_instruction_title)
                typeface = tinosBold
                setTextColor(this@DialogHelper.context.getColor(R.color.secondPrimary))
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setPadding(
                    15.dpToPx(context),
                    15.dpToPx(context),
                    15.dpToPx(context),
                    0
                )
                textSize = this@DialogHelper.context.resources.getDimension(R.dimen.big_title) / this@DialogHelper.context.resources.displayMetrics.density
            }

            val contentView = TextView(this@DialogHelper.context).apply {
                typeface = tinosRegular

                val formattedString = SpannableString(content)
                formattedString.setSpan(StyleSpan(Typeface.BOLD), 0, content.indexOf("\n\n"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                text = formattedString
                setTextColor(this@DialogHelper.context.getColor(R.color.black))
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setPadding(
                    15.dpToPx(context),
                    15.dpToPx(context),
                    15.dpToPx(context),
                    0
                )
            }

            setCustomTitle(titleView)
            setView(contentView)
            setCancelable(false)
            setPositiveButton("Đã hiểu") { dialog, _ ->
                positiveAction?.invoke()
                dialog.dismiss()
            }
        }.create()
    }

    fun getLogOutDialog(
        positiveAction: (() -> Unit)?
    ): AlertDialog{
        return AlertDialog.Builder(context, R.style.MyAlertDialog).apply {
            val titleView = TextView(this@DialogHelper.context).apply {
                text = this@DialogHelper.context.getString(R.string.Ask_for_logout)
                typeface = tinosBold
                setTextColor(this@DialogHelper.context.getColor(R.color.secondPrimary))
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setPadding(
                    15.dpToPx(context),
                    15.dpToPx(context),
                    15.dpToPx(context),
                    0
                )
                textSize = this@DialogHelper.context.resources.getDimension(R.dimen.big_title).pxToDp(context)
            }

            setCustomTitle(titleView)
            setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Đăng xuất") { dialog, _ ->
                positiveAction?.invoke()
                dialog.dismiss()
            }
        }.create()
    }

    fun getExamLockedDialog(): Dialog{
        return AlertDialog.Builder(context, R.style.MyAlertDialog).apply {
            val titleView = TextView(this@DialogHelper.context).apply {
                text = this@DialogHelper.context.getString(R.string.Notify_locked_exam)
                typeface = tinosBold
                setTextColor(this@DialogHelper.context.getColor(R.color.secondPrimary))
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setPadding(
                    15.dpToPx(context),
                    15.dpToPx(context),
                    15.dpToPx(context),
                    0
                )
                textSize = this@DialogHelper.context.resources.getDimension(R.dimen.big_title).pxToDp(context)
            }

            val contentView = TextView(this@DialogHelper.context).apply {
                text = this@DialogHelper.context.getString(R.string.Require_user_upgrade_account)
                typeface = tinosRegular
                setTextColor(this@DialogHelper.context.getColor(R.color.black))
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setPadding(
                    15.dpToPx(context),
                    15.dpToPx(context),
                    15.dpToPx(context),
                    0
                )
            }

            setCustomTitle(titleView)
            setView(contentView)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        }.create()
    }
}