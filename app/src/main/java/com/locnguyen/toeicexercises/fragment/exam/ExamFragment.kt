package com.locnguyen.toeicexercises.fragment.exam

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.adapter.ExamQuestionsAdapter
import com.locnguyen.toeicexercises.databinding.ExamFragmentBinding
import com.locnguyen.toeicexercises.model.Exam
import com.locnguyen.toeicexercises.model.Question
import com.locnguyen.toeicexercises.utils.DialogHelper
import com.locnguyen.toeicexercises.viewmodel.ExamVM
import com.locnguyen.toeicexercises.viewmodel.main.MainVM
import java.util.Locale
import kotlin.properties.Delegates

class ExamFragment: Fragment() {

    private lateinit var binding: ExamFragmentBinding
    private lateinit var exam: Exam
    private lateinit var mainVM: MainVM
    private lateinit var examVM: ExamVM
    private lateinit var navController: NavController
    private lateinit var examQuestionAdapter: ExamQuestionsAdapter
    private var examTimeLeft by Delegates.notNull<Long>()
    private lateinit var allQuestions: List<Pair<String, Question>>

    private var examTimer: CountDownTimer? = null
    private var currentPart: String = ""

    private val dialogHelper: DialogHelper by lazy {DialogHelper(requireContext())}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExamFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        mainVM = ViewModelProvider(requireActivity())[MainVM::class.java]
        examVM = ViewModelProvider(requireActivity())[ExamVM::class.java]
        binding.examVM = examVM

        exam = examVM.exam.value ?: Exam()

        examTimeLeft = (exam.time * 60 * 1000).toLong()

        allQuestions = examVM.getAllQuestionsFollowEachPart(requireContext())
        examVM.examQuestions = examVM.getAllQuestionsFollowEachPart(requireContext()) as ArrayList<Pair<String, Question>>

        examQuestionAdapter = ExamQuestionsAdapter(requireActivity(), allQuestions)

        startCountDown()

        initViews()
        initListeners()
        initObserves()
    }

    private fun initViews() {
        binding.title.text = exam.title
        binding.questions.apply {
            adapter = examQuestionAdapter
            registerOnPageChangeCallback(handleQuestionChangeCallback())
        }
    }

    private fun handleQuestionChangeCallback() = object: ViewPager2.OnPageChangeCallback(){
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            val quesPart = allQuestions[position].first
            val isNewPart = (quesPart != currentPart)

            // Chỉ hiển thị hộp thoại hướng dẫn khi chuyển sang câu tiếp theo phía sau
            if (isNewPart && (position > examVM.currentQuestion.value!! || position == 0)){
                pauseCountDown()

                DialogHelper(requireContext()).getExamInstructionDialog(allQuestions[position].first) {
                    startCountDown()
                    currentPart = quesPart
                }.show()
            }

            binding.orderQuestion.text = requireContext().getString(R.string.Order_question_in_list_question_regex, position+1, examVM.examQuestions.size)
            examVM.currentQuestion.value = position
        }
    }

    private fun initListeners(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                handlePressedBack()
            }
        })

        binding.icBack.setOnClickListener {
            handlePressedBack()
        }

        binding.icFinish.setOnClickListener {
            dialogHelper.getFinishedExamDialog {
                handleFinishedExam()
            }.show()
        }

        binding.icPreviousQuestion.setOnClickListener {
            examVM.currentQuestion.value = examVM.currentQuestion.value!! - 1
        }

        binding.icNextQuestion.setOnClickListener {
            examVM.currentQuestion.value = examVM.currentQuestion.value!! + 1
        }
    }

    private fun initObserves(){
        examVM.currentQuestion.observe(viewLifecycleOwner){value ->
            binding.questions.currentItem = value ?: 0
        }
    }

    private fun startCountDown(){
        examTimer = object: CountDownTimer(examTimeLeft, 1000){
            override fun onTick(millisUntilFinished: Long) {
                examTimeLeft = millisUntilFinished

                val seconds = (millisUntilFinished / 1000) % 60
                val minutes = ((millisUntilFinished / (1000 * 60)) % 60)
                val hours = ((millisUntilFinished / (1000 * 60 * 60)) % 24)

                val timeFormat = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)

                binding.timeRemaining.text = timeFormat
            }

            override fun onFinish() {
                handleFinishedExam()
            }
        }.start()
    }

    private fun pauseCountDown(){
        examTimer?.cancel()
    }

    private fun handleFinishedExam(){
        examVM.calculateExamScore()
        examQuestionAdapter.removeAllQuestions()
        binding.questions.apply {
            adapter = null
            unregisterOnPageChangeCallback(handleQuestionChangeCallback())
        }
        navController.navigate(R.id.action_examFragment_to_scoreFragment)
    }

    private fun handlePressedBack(){
        navController.popBackStack()
        binding.questions.apply {
            adapter = null
            unregisterOnPageChangeCallback(handleQuestionChangeCallback())
        }
        mainVM.itemExamClicked.value = null
        examVM.apply {
            exam.value = null
            currentQuestion.value = 0
            examQuestions.clear()
            userAnswers.clear()
            listenCorrectAnswers = emptyList()
            readCorrectAnswers = emptyList()
            answerCorrect.clear()
        }
    }
}