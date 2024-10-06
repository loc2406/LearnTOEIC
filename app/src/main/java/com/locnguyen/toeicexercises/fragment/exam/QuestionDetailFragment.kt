package com.locnguyen.toeicexercises.fragment.exam

import android.app.Dialog
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.QuestionDetailFragmentBinding
import com.locnguyen.toeicexercises.model.Question
import com.locnguyen.toeicexercises.utils.DialogHelper
import com.locnguyen.toeicexercises.utils.GlobalHelper
import com.locnguyen.toeicexercises.utils.dpToPx
import com.locnguyen.toeicexercises.utils.loadImg
import com.locnguyen.toeicexercises.viewmodel.ExamVM
import java.util.Locale
import kotlin.properties.Delegates

class QuestionDetailFragment : Fragment(), Runnable {
    private lateinit var binding: QuestionDetailFragmentBinding
    private lateinit var question: Question
    private lateinit var answerViews: List<TextView>
    private val examVM: ExamVM by activityViewModels<ExamVM>()
    private var isShowAnswer by Delegates.notNull<Boolean>()

    private var questionPosition: Int = -1
    private var currentSecondsPlayed: Int = 0
    private var totalMediaSeconds: Int = 0

    private val loadingDialog: Dialog by lazy { DialogHelper.getLoadingDialog(requireContext()) }
    private val mediaPlayer: MediaPlayer by lazy { MediaPlayer() }
    private val currentMediaState: MutableLiveData<MediaState> by lazy { MutableLiveData(MediaState.IDLE) }
    private val handler: Handler by lazy { Handler(Looper.getMainLooper()) }
    private val loadImgSuccess: MutableLiveData<Boolean> by lazy { MutableLiveData(false) }
    private val loadAudioSuccess: MutableLiveData<Boolean> by lazy { MutableLiveData(false) }
    private val loadDataSuccess: MediatorLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(loadImgSuccess) { isLoaded ->
                this.value = isLoaded && loadAudioSuccess.value!! == true
            }
            addSource(loadAudioSuccess) { isLoaded ->
                this.value = isLoaded && loadImgSuccess.value!! == true
            }
        }
    }
    private val userAnswer: MutableLiveData<TextView> = MutableLiveData()

    enum class MediaState {
        IDLE, INITIALIZED, PREPARED, STARTED, PAUSED, PLAYBACK_COMPLETED, STOPPED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = QuestionDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        question = getQuestionFromArgument() ?: Question()
        questionPosition = arguments?.getInt("POSITION") ?: -1
        isShowAnswer = arguments?.getBoolean("IS_SHOW_ANSWER") ?: false

        initViews()
        initListeners()
        initObserves()
    }

    private fun getQuestionFromArgument(): Question? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("QUESTION", Question::class.java)
        } else {
            arguments?.getParcelable("QUESTION")
        }
    }

    private fun initViews() {
        initMedia()

        answerViews = listOf(
            binding.firstAnswer,
            binding.secondAnswer,
            binding.thirdAnswer,
            binding.fourthAnswer
        )

        initAnswerViews()
        initAnswerContents()

        binding.title.text = requireContext().getString(
            R.string.Question_title_regex,
            questionPosition + 1,
            question.title
        )

        if(question.img.isEmpty()){
            loadImgSuccess.value = true
            binding.img.visibility = GONE
        }
        else{
            requireContext().loadImg(
                question.img,
                binding.img,
                object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.img.visibility = GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        loadImgSuccess.value = true
                        return false
                    }
                })
        }

        checkUserAnsweredBefore()
    }

    private fun initMedia() {
        if (question.media.isNotEmpty()) {
            try {
                binding.mediaProgress.isEnabled = false
                mediaPlayer.setDataSource(question.media)
                currentMediaState.value = MediaState.INITIALIZED
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    loadAudioSuccess.value = true
                    mediaPlayer.isLooping = false
                    currentMediaState.value = MediaState.PREPARED
                    totalMediaSeconds = mediaPlayer.duration / 1000
                    updateMediaTime(currentSecondsPlayed/60, currentSecondsPlayed%60, totalMediaSeconds/60, totalMediaSeconds%60)
                    binding.mediaProgress.max = totalMediaSeconds
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            binding.mediaSpace.visibility = GONE
            loadAudioSuccess.value = true
        }
    }

    private fun initAnswerViews(){
        if (isShowAnswer){
            binding.firstAnswer.apply {
                layoutParams = ConstraintLayout.LayoutParams(
                    0,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topToBottom = binding.img.id
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    topMargin = 20.dpToPx(requireContext())
                }

                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_rectangle_primary_stroke_10dp_corners)
                setPadding(10.dpToPx(requireContext()), 10.dpToPx(requireContext()), 10.dpToPx(requireContext()), 10.dpToPx(requireContext()))
            }
            binding.secondAnswer.apply {
                layoutParams = ConstraintLayout.LayoutParams(
                    0,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topToBottom = binding.firstAnswer.id
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    topMargin = 10.dpToPx(requireContext())
                }

                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_rectangle_primary_stroke_10dp_corners)
                setPadding(10.dpToPx(requireContext()), 10.dpToPx(requireContext()), 10.dpToPx(requireContext()), 10.dpToPx(requireContext()))
            }
            binding.thirdAnswer.apply {
                layoutParams = ConstraintLayout.LayoutParams(
                    0,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topToBottom = binding.secondAnswer.id
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    topMargin = 10.dpToPx(requireContext())
                }

                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_rectangle_primary_stroke_10dp_corners)
                setPadding(10.dpToPx(requireContext()), 10.dpToPx(requireContext()), 10.dpToPx(requireContext()), 10.dpToPx(requireContext()))
            }
            binding.fourthAnswer.apply {
                layoutParams = ConstraintLayout.LayoutParams(
                    0,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topToBottom = binding.thirdAnswer.id
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    topMargin = 10.dpToPx(requireContext())
                }

                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_rectangle_primary_stroke_10dp_corners)
                setPadding(10.dpToPx(requireContext()), 10.dpToPx(requireContext()), 10.dpToPx(requireContext()), 10.dpToPx(requireContext()))
            }
        }
        else{
            binding.firstAnswer.apply {
                layoutParams = ConstraintLayout.LayoutParams(
                    30.dpToPx(requireContext()),
                    30.dpToPx(requireContext())
                ).apply {
                    topToBottom = binding.img.id
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    endToStart = binding.secondAnswer.id
                    topMargin = 20.dpToPx(requireContext())
                }

                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_circle_answer)
                gravity = Gravity.CENTER
            }
            binding.secondAnswer.apply {
                layoutParams = ConstraintLayout.LayoutParams(
                    30.dpToPx(requireContext()),
                    30.dpToPx(requireContext())
                ).apply {
                    topToTop = binding.firstAnswer.id
                    startToEnd = binding.firstAnswer.id
                    endToStart = binding.thirdAnswer.id
                    bottomToBottom = binding.firstAnswer.id
                }

                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_circle_answer)
                gravity = Gravity.CENTER

            }
            binding.thirdAnswer.apply {
                layoutParams = ConstraintLayout.LayoutParams(
                    30.dpToPx(requireContext()),
                    30.dpToPx(requireContext())
                ).apply {
                    topToTop = binding.secondAnswer.id
                    startToEnd = binding.secondAnswer.id
                    endToStart = binding.fourthAnswer.id
                    bottomToBottom = binding.secondAnswer.id
                }

                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_circle_answer)
                gravity = Gravity.CENTER
            }
            binding.fourthAnswer.apply {
                layoutParams = ConstraintLayout.LayoutParams(
                    30.dpToPx(requireContext()),
                    30.dpToPx(requireContext())
                ).apply {
                    topToTop = binding.thirdAnswer.id
                    startToEnd = binding.thirdAnswer.id
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    bottomToBottom = binding.thirdAnswer.id
                }

                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_circle_answer)
                gravity = Gravity.CENTER
            }
        }
    }

    private fun initAnswerContents(){
        val answersChar = listOf("A", "B", "C", "D")

        for (i in question.answers.indices) {
            answerViews[i].apply {
                text = if (isShowAnswer){
                    requireContext().getString(
                        R.string.Question_answer_with_content_regex,
                        answersChar[i],
                        question.answers[i]
                    )
                }else{
                    requireContext().getString(
                        R.string.Question_answer_no_content_regex,
                        answersChar[i]
                    )
                }

                visibility = VISIBLE

                setOnClickListener {
                    userAnswer.value = this
                }
            }
        }

        answerViews = answerViews.filter { view -> view.visibility == VISIBLE }
    }

    private fun initListeners() {
        if (question.media.isNotEmpty()) {
            binding.icControlMedia.setOnClickListener {
                when (currentMediaState.value) {
                    MediaState.STARTED -> currentMediaState.value = MediaState.PAUSED
                    MediaState.PAUSED, MediaState.PREPARED -> currentMediaState.value = MediaState.STARTED

                    MediaState.STOPPED -> {
                        stopMediaPlayer()
                        currentSecondsPlayed = 0
                        binding.mediaProgress.progress = 0
                        loadAudioSuccess.value = false
                        mediaPlayer.prepareAsync()
                    }

                    else -> {}
                }
            }

            mediaPlayer.setOnCompletionListener {
                currentMediaState.value = MediaState.PLAYBACK_COMPLETED
            }
        }
    }

    private fun updateMediaTime(minutesPlayed: Int, secondsPlayed: Int, totalMinutes: Int, totalSeconds: Int){
        binding.mediaTime.post {
            binding.mediaTime.text = requireContext().getString(
                R.string.Media_time_regex,
                getMediaTime(minutesPlayed, secondsPlayed),
                getMediaTime(totalMinutes, totalSeconds)
            )
        }
    }

    private fun initObserves() {
        loadDataSuccess.observe(viewLifecycleOwner) { isLoaded ->
            if (isLoaded) {
                loadingDialog.dismiss()
            } else {
                loadingDialog.show()
            }
        }

        currentMediaState.observe(viewLifecycleOwner) { state ->
            when (state) {
                MediaState.PREPARED -> {
                    binding.icControlMedia.setImageResource(R.drawable.ic_play)
                }

                MediaState.STARTED -> {
                    binding.icControlMedia.setImageResource(R.drawable.ic_pause)
                    startMediaPlayer()
                }

                MediaState.PAUSED -> {
                    binding.icControlMedia.setImageResource(R.drawable.ic_play)
                    pauseMediaPlayer()
                }

                MediaState.PLAYBACK_COMPLETED -> {
                    binding.icControlMedia.setImageResource(R.drawable.ic_repeat)
                    currentMediaState.value = MediaState.STOPPED
                }

                else -> {}
            }
        }

        userAnswer.observe(viewLifecycleOwner) { view ->
            view?.let {
                checkedAnswer(view)

                val answer: String = try{
                    when{
                        view.text.startsWith('A') -> question.answers[0]
                        view.text.startsWith('B') -> question.answers[1]
                        view.text.startsWith('C') -> question.answers[2]
                        view.text.startsWith('D') -> question.answers[3]
                        else -> ""
                    }
                }catch (e: IndexOutOfBoundsException){
                    ""
                }

                if (examVM.userAnswers.contains(questionPosition)) {
                    examVM.userAnswers.replace(questionPosition, answer)
                } else {
                    examVM.userAnswers[questionPosition] = answer
                }

                answerViews.forEach { answerView ->
                    if (answerView.text != view.text) {
                        uncheckedAnswer(answerView)
                    }
                }
            }
        }
    }

    private fun startMediaPlayer() {
        mediaPlayer.start()
        handler.post(this)
    }

    private fun pauseMediaPlayer(){
        mediaPlayer.pause()
        handler.removeCallbacks(this)
    }

    private fun stopMediaPlayer(){
        mediaPlayer.stop()
        handler.removeCallbacks(this)
    }

    private fun checkUserAnsweredBefore() {
        if (examVM.userAnswers.containsKey(questionPosition)) {
            answerViews.forEach { answerView ->
                if (answerView.text == examVM.userAnswers[questionPosition]) {
                    userAnswer.value = answerView
                }
            }
        }
    }

    private fun getMediaTime(minutes: Int, seconds: Int): String {
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    private fun checkedAnswer(view: TextView) {
        view.apply {
            setTextColor(WHITE)
            background = if (isShowAnswer){
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.bg_primary_rectangle_no_stroke_10dp_corners
                )
            }
            else{
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.bg_circle_true_answer
                )
            }
        }
    }

    private fun uncheckedAnswer(view: TextView) {
        view.apply {
            setTextColor(BLACK)
            background = if (isShowAnswer){
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.bg_white_rectangle_primary_stroke_10dp_corners
                )
            }
            else{
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.bg_circle_answer
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer.isPlaying){
            currentMediaState.value = MediaState.PAUSED
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }

    override fun run() {
        val totalPlayed = currentSecondsPlayed
        val minutesPlayed = totalPlayed / 60
        val secondsPlayed = totalPlayed % 60

        updateMediaTime(minutesPlayed, secondsPlayed, totalMediaSeconds/60, totalMediaSeconds%60)
        binding.mediaProgress.progress = totalPlayed

        if (currentSecondsPlayed < totalMediaSeconds){
            currentSecondsPlayed += 1
            handler.postDelayed(this, 1000)
        }
    }
}