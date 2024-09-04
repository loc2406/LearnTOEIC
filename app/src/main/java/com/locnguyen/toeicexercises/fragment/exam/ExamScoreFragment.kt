package com.locnguyen.toeicexercises.fragment.exam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.ExamScoreFragmentBinding
import com.locnguyen.toeicexercises.utils.DialogHelper
import com.locnguyen.toeicexercises.viewmodel.ExamVM
import com.locnguyen.toeicexercises.viewmodel.MainVM
import java.util.Locale

class ExamScoreFragment : Fragment() {
    private lateinit var binding: ExamScoreFragmentBinding
    private lateinit var navController: NavController
    private lateinit var mainVM: MainVM
    private lateinit var examVM: ExamVM


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExamScoreFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        mainVM = ViewModelProvider(requireActivity())[MainVM::class.java]
        examVM = ViewModelProvider(requireActivity())[ExamVM::class.java]
        val examScore = examVM.getExamScore()

        initViews(examScore)
        initListeners()
    }

    private fun initViews(examScore: Int) {
        when (examScore){
            in 10..400 -> binding.scoreValue.setTextColor(requireContext().getColor(R.color.red))
            in 405..780 -> binding.scoreValue.setTextColor(requireContext().getColor(R.color.orange))
            else /* in 785..990*/-> binding.scoreValue.setTextColor(requireContext().getColor(R.color.green))
        }

        binding.scoreValue.text = examScore.toString()

        initListenChart()
        initReadChart()
    }

    private fun initListenChart() {
        val correctPercents: Float = (examVM.listenCorrectAnswers.size.toFloat() / examVM.examQuestions.size) * 100
        val listenEntries: ArrayList<PieEntry> = ArrayList<PieEntry>().apply {
            add(PieEntry(correctPercents, requireContext().getString(R.string.Answer_correct, examVM.listenCorrectAnswers.size, examVM.examQuestions.size)))
            add(PieEntry(100 - correctPercents, requireContext().getString(R.string.Answer_incorrect, examVM.examQuestions.size - examVM.listenCorrectAnswers.size, examVM.examQuestions.size)))
        }
        val dataSet = PieDataSet(listenEntries, "").apply {
            sliceSpace = 0f // Không có khoảng cách giữa các entry với nhau trong biểu đồ
            valueTextColor = android.graphics.Color.WHITE
            valueTextSize = 14f
            valueTypeface = ResourcesCompat.getFont(requireContext(), R.font.tinos_bold)
            colors = mutableListOf(
                requireContext().getColor(R.color.green),
                requireContext().getColor(R.color.red))
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    // Hiển thị giá trị thành phần trăm nếu đó là số câu trả lời đúng và chuỗi rỗng nếu là câu trả lời sai
                    return if (value == correctPercents) String.format(Locale.getDefault(), "%.02f", correctPercents) + "%" else ""
                }
            }
        }
        binding.listeningChart.legend.apply {
            form = Legend.LegendForm.CIRCLE
            direction = Legend.LegendDirection.LEFT_TO_RIGHT // hiển thị chú thích theo trái -> phải
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER // căn giữa dòng chú thích
            orientation = Legend.LegendOrientation.VERTICAL // hiển thị chú thích theo chiều dọc
        }
        binding.listeningChart.apply {
            data = PieData(dataSet)
            setDrawEntryLabels(false) // Không hiện label trên biểu đồ
            isDrawHoleEnabled = false // không vẽ lỗ hình tròn ở giữa biểu đồ
            // holeRadius = 10f Bán kính lỗ hình tròn ở giữa bằng 10% bán kính biểu đồ
            setTransparentCircleAlpha(0) // Không hiện hình tròn với alpha mờ ở giữa biểu đồ
            description = Description().apply { isEnabled = false } // Không hiện description của dữ liệu bên cạnh biểu đồ
            invalidate()
        }
    }

    private fun initReadChart() {
        val correctPercents: Float = (examVM.readCorrectAnswers.size.toFloat() / examVM.examQuestions.size) * 100
        val readEntries: ArrayList<PieEntry> = ArrayList<PieEntry>().apply {
            add(PieEntry(correctPercents, requireContext().getString(R.string.Answer_correct, examVM.readCorrectAnswers.size, examVM.examQuestions.size)))
            add(PieEntry(100 - correctPercents, requireContext().getString(R.string.Answer_incorrect, examVM.examQuestions.size - examVM.readCorrectAnswers.size, examVM.examQuestions.size)))
        }
        val dataSet = PieDataSet(readEntries, "").apply {
            sliceSpace = 0f // Không có khoảng cách giữa các entry với nhau trong biểu đồ
            valueTextColor = android.graphics.Color.WHITE
            valueTextSize = 14f
            valueTypeface = ResourcesCompat.getFont(requireContext(), R.font.tinos_bold)
            colors = mutableListOf(
                requireContext().getColor(R.color.green),
                requireContext().getColor(R.color.red))
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    // Hiển thị giá trị thành phần trăm nếu đó là số câu trả lời đúng và chuỗi rỗng nếu là câu trả lời sai
                    return if (value == correctPercents) String.format(Locale.getDefault(), "%.02f", correctPercents) + "%" else ""
                }
            }
        }
        binding.readingChart.legend.apply {
            form = Legend.LegendForm.CIRCLE
            direction = Legend.LegendDirection.LEFT_TO_RIGHT // hiển thị chú thích theo trái -> phải
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER // căn giữa dòng chú thích
            orientation = Legend.LegendOrientation.VERTICAL // hiển thị chú thích theo chiều dọc
        }
        binding.readingChart.apply {
            data = PieData(dataSet)
            setDrawEntryLabels(false) // Không hiện label trên biểu đồ
            isDrawHoleEnabled = false // không vẽ lỗ hình tròn ở giữa biểu đồ
            // holeRadius = 10f Bán kính lỗ hình tròn ở giữa bằng 10% bán kính biểu đồ
            setTransparentCircleAlpha(0) // Không hiện hình tròn với alpha mờ ở giữa biểu đồ
            description = Description().apply { isEnabled = false } // Không hiện description của dữ liệu bên cạnh biểu đồ
            invalidate()
        }
    }

    private fun initListeners(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                handlePressedBack()
            }
        })

        binding.backMainScreenSpace.setOnClickListener {
            handlePressedBack()
        }

        binding.answersDetail.setOnClickListener{
            navController.navigate(R.id.action_examScoreFragment_to_examAnswerFragment)
        }
    }

    private fun handlePressedBack() {
        navController.popBackStack(R.id.mainFragment, false)
        resetExamData()
    }

    private fun resetExamData() {
        mainVM.itemExamClicked.value = null
        examVM.apply {
            examQuestions.clear()
            userAnswers.clear()
            listenCorrectAnswers = emptyList()
            readCorrectAnswers = emptyList()
            answerCorrect.clear()
        }
    }
}