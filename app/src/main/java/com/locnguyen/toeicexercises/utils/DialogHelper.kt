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
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.reflect.TypeToken
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.MyBottomSheetDialogBinding
import com.locnguyen.toeicexercises.model.Word
import com.locnguyen.toeicexercises.model.WordKindMean

class DialogHelper(private val context: Context) {

    private val tinosRegular = ResourcesCompat.getFont(context, R.font.tinos_regular)
    private val tinosBold = ResourcesCompat.getFont(context, R.font.tinos_bold)
    private val tinosItalic = ResourcesCompat.getFont(context, R.font.tinos_italic)

    fun getLoadingDialog(): Dialog {
        return Dialog(context).apply {
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
    }

    fun getWordMeanDialog(word: Word): BottomSheetDialog {
        val dialogView = MyBottomSheetDialogBinding.inflate(LayoutInflater.from(context), null, false)

        dialogView.title.text = word.title

        val listWordKindMeanType = object : TypeToken<List<WordKindMean>>(){}.type

//        for (means in word.means) {
//
//            val kindView = TextView(context).apply {
//                layoutParams = ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//                )
//                text = means.vietnameseKind()
//                textSize = 16f
//                setTextColor(context.getColor(R.color.black))
//                typeface = tinosBold
//            }
//
//            dialogView.contentSpace.addView(kindView)
//
//            for (mean in means.means) {
//
//                val wordMeanView = TextView(context).apply {
//                    layoutParams = LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT
//                    ).apply {
//                        setMargins(
//                            10 * context.resources.displayMetrics.density.toInt(),
//                            0,
//                            0,
//                            0
//                        )
//                    }
//                    text = context.getString(R.string.Word_mean_regex, mean.first)
//                    textSize = 14f
//                    setTextColor(context.getColor(R.color.secondPrimary))
//                    typeface = tinosBold
//                }
//
//                dialogView.contentSpace.addView(wordMeanView)
//
//                val wordMeanExView = TextView(context).apply {
//                    layoutParams = LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT
//                    ).apply {
//                        setMargins(
//                            20 * context.resources.displayMetrics.density.toInt(),
//                            0,
//                            0,
//                            0
//                        )
//                    }
//                    text = context.getString(
//                        R.string.Word_mean_example_regex,
//                        MainFragment.listExample.find { ex -> ex.id == mean.second }?.content ?: ""
//                    )
//                    textSize = 14f
//                    setTextColor(context.getColor(R.color.black))
//                    typeface = tinosItalic
//                }
//
//                dialogView.contentSpace.addView(wordMeanExView)
//            }
//
//        }

        return BottomSheetDialog(context).apply {
            setContentView(dialogView.root)
            (dialogView.root.parent as View?)?.background = ColorDrawable(Color.TRANSPARENT)
        }
    }

    fun getFinishedExamDialog(
        negativeAction: (() -> Unit)?,
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
                    (15 * this@DialogHelper.context.resources.displayMetrics.density).toInt(),
                    (15 * this@DialogHelper.context.resources.displayMetrics.density).toInt(),
                    (15 * this@DialogHelper.context.resources.displayMetrics.density).toInt(),
                    0
                )
                textSize =
                    this@DialogHelper.context.resources.getDimension(R.dimen.big_title) / this@DialogHelper.context.resources.displayMetrics.density
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
                    (15 * this@DialogHelper.context.resources.displayMetrics.density).toInt(),
                    (15 * this@DialogHelper.context.resources.displayMetrics.density).toInt(),
                    (15 * this@DialogHelper.context.resources.displayMetrics.density).toInt(),
                    0
                )
            }

            setCustomTitle(titleView)
            setView(contentView)
            setNegativeButton("Hủy") { dialog, _ ->
                negativeAction?.invoke()
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
                    (15 * this@DialogHelper.context.resources.displayMetrics.density).toInt(),
                    (15 * this@DialogHelper.context.resources.displayMetrics.density).toInt(),
                    (15 * this@DialogHelper.context.resources.displayMetrics.density).toInt(),
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
                    (15 * this@DialogHelper.context.resources.displayMetrics.density).toInt(),
                    (15 * this@DialogHelper.context.resources.displayMetrics.density).toInt(),
                    (15 * this@DialogHelper.context.resources.displayMetrics.density).toInt(),
                    0
                )
            }

            setCustomTitle(titleView)
            setView(contentView)
            setPositiveButton("Đã hiểu") { dialog, _ ->
                positiveAction?.invoke()
                dialog.dismiss()
            }
        }.create()
    }
}