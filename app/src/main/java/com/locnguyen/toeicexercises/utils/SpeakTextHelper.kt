package com.locnguyen.toeicexercises.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.locnguyen.toeicexercises.R
import java.io.IOException
import java.util.Locale
import java.util.UUID

class SpeakTextHelper(private val context: Context) : UtteranceProgressListener(),
    TextToSpeech.OnInitListener, MediaPlayer.OnCompletionListener {

    private var isSupported: Boolean = true
    private var successListener: (() -> Unit)? = null
    var isUseSpeed: Boolean = false // trạng thái có đang đọc hay không

    private val textToSpeech: TextToSpeech by lazy { TextToSpeech(context, this) }
    private val mediaPlayer: MediaPlayer by lazy { MediaPlayer() }
    //private var preferenceHelper: PreferenceHelper by lazy { PreferenceHelper(context) }

    override fun onStart(utteranceId: String?) {

    }

    override fun onDone(utteranceId: String?) {

    }

    override fun onError(utteranceId: String?) {

    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            try{
                val result = textToSpeech.setLanguage(Locale.ENGLISH)
                isSupported = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
            }catch(e: IllegalArgumentException){
                e.printStackTrace()
            }
        }else{
            isSupported = false
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {

    }

    fun stop(){
        if (mediaPlayer.isPlaying) mediaPlayer.stop()
        if (textToSpeech.isSpeaking) textToSpeech.stop()
    }

    fun speakText(text: String?, languageCode: String? = null) {
        if (text == null || text.trim().isEmpty()) {
            return
        } else {
            if (context.isHasNetWork()) {
                playAudio(null, text, languageCode)
            } else {
                textToSpeech(text, languageCode)
            }
        }
    }

    private fun playAudio(audioUrl: String?, text: String?, languageCode: String?) {
        try {
            if (textToSpeech.isSpeaking) {
                textToSpeech.stop()
            }

            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }

            mediaPlayer.reset()
            mediaPlayer.setDataSource(
                if (audioUrl == null || audioUrl.trim().isEmpty()) {
                    "https://translate.google.com/translate_tts?ie=UTF-8&client=tw-ob&q=$text&tl=${languageCode ?: "en"}"
                } else {
                    audioUrl
                }
            )
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                it.start()
                if (isUseSpeed) {
                    updateAudio()
                }
            }
            mediaPlayer.setOnCompletionListener(this)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mediaPlayer.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
            } else {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            }

        } catch (e: IOException) {
            e.printStackTrace()
            textToSpeech(text, languageCode)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            textToSpeech(text, languageCode)
        }
    }

    private fun updateAudio() {
//        if (preferencesHelper == null) return
//        if (mp.isPlaying) {
//            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    val speed: Float = 1f - (5 - preferencesHelper!!.audioSpeed) * .1f
//                    mp.playbackParams = mp.playbackParams.setSpeed(speed)
//                }
//            } catch (e: java.lang.IllegalStateException) {
//                e.printStackTrace()
//            }
//        }
//        if (tts != null && tts!!.isSpeaking) {
//            val speed: Float = 1f - (5 - preferencesHelper!!.audioSpeed) * .1f
//            tts!!.setSpeechRate(speed)
//        }

        // Trong preferenceHelper
//        var audioSpeed: Int
//        get() {
//            return sharedPreferences.getInt("audioSpeed", 5)
//        }
//        set(value) {
//            sharedPreferences.edit().putInt("audioSpeed", value).apply()
//        }

    }

    private fun textToSpeech(text: String?, languageCode: String?) {
        if (isSupported) {
            if (!text.isNullOrEmpty()) {
                try {
                    if (mediaPlayer.isPlaying) mediaPlayer.stop()
                    if (textToSpeech.isSpeaking) textToSpeech.stop()

                    textToSpeech.setOnUtteranceProgressListener(this)
                    languageCode?.let { textToSpeech.language = Locale(it) }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        textToSpeech.speak(
                            text,
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            UUID.randomUUID().toString()
                        )
                    } else {
                        val myHashAlarm = HashMap<String, String>()
                        myHashAlarm[TextToSpeech.Engine.KEY_PARAM_STREAM] = AudioManager.STREAM_MUSIC.toString()
                        myHashAlarm[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = text
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, myHashAlarm)
                    }

                    if (isUseSpeed){
                        updateAudio()
                    }
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                }
            } else {

            }
        } else {
            context.toastMessage(R.string.message_is_not_supported_text_to_speech)
        }
    }
}