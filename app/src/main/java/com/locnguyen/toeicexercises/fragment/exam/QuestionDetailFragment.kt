package com.locnguyen.toeicexercises.fragment.exam

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnSeekCompleteListener
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.QuestionDetailFragmentBinding
import com.locnguyen.toeicexercises.fragment.BaseFragment
import com.locnguyen.toeicexercises.model.Question
import com.locnguyen.toeicexercises.utils.DialogHelper
import com.locnguyen.toeicexercises.utils.GlobalHelper
import com.locnguyen.toeicexercises.viewmodel.ExamVM
import java.util.Locale

class QuestionDetailFragment : Fragment(), Runnable {
    private lateinit var binding: QuestionDetailFragmentBinding
    private lateinit var question: Question
    private lateinit var answerViews: List<TextView>
    private lateinit var examVM: ExamVM

    private var questionPosition: Int = -1
    private var currentSecondsPlayed: Int = 0
    private var totalMediaSeconds: Int = 0

    private val dialogHelper: DialogHelper by lazy { DialogHelper(requireContext()) }
    private val loadingDialog: Dialog by lazy { dialogHelper.getLoadingDialog() }
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
        IDLE, INITIALIZED, PREPARED, STARTED, PAUSED, STOPPED, PLAYBACK_COMPLETED
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
        questionPosition = arguments?.getInt("POSITION", -1) ?: -1
        answerViews = listOf(
            binding.firstAnswer,
            binding.secondAnswer,
            binding.thirdAnswer,
            binding.fourthAnswer
        )
        examVM = ViewModelProvider(requireActivity())[ExamVM::class.java]

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
        setMedia()

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
            GlobalHelper(requireContext()).loadImg(
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

        val answersChar = listOf("A", "B", "C", "D")
        for (i in question.answers.indices) {
            answerViews[i].apply {
                text = requireContext().getString(
                    R.string.Question_answer_regex,
                    answersChar[i],
                    question.answers[i]
                )

                visibility = VISIBLE

                setOnClickListener {
                    userAnswer.value = this
                }
            }
        }

        checkUserAnsweredBefore()
    }

    private fun setMedia() {
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
        }
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

                if (examVM.userAnswers.contains(questionPosition)) {
                    // không lấy các kí tự "A. ", "B. ", "C. ", "D. "
                    examVM.userAnswers.replace(questionPosition, view.text.toString().substring(3))
                } else {
                    // không lấy các kí tự "A. ", "B. ", "C. ", "D. "
                    examVM.userAnswers[questionPosition] = view.text.toString().substring(3)
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
            background = AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.bg_primary_rectangle_no_stroke_10dp_corners
            )
        }
    }

    private fun uncheckedAnswer(view: TextView) {
        view.apply {
            setTextColor(BLACK)
            background = AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.bg_white_rectangle_primary_stroke_10dp_corners
            )
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