package com.locnguyen.toeicexercises.fragment.word

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.WordFragmentBinding
import com.locnguyen.toeicexercises.model.Word
import com.locnguyen.toeicexercises.utils.GlobalHelper
import com.locnguyen.toeicexercises.utils.dpToPx
import com.locnguyen.toeicexercises.utils.pxToDp
import com.locnguyen.toeicexercises.utils.toastMessage
import com.locnguyen.toeicexercises.viewmodel.WordVM

class WordFragment: Fragment() {
    private lateinit var binding: WordFragmentBinding
    private lateinit var word: Word
    private lateinit var navController: NavController

    private var isFavorite: Boolean = false

    private val args by navArgs<WordFragmentArgs>()
    private val wordVM: WordVM by activityViewModels<WordVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WordFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        word = args.word

        initViews()
        initListeners()
        initObserves()
    }

    private fun initViews() {
        binding.title.text = word.title

        wordVM.favWords.value.takeIf { it?.contains(word) == true }?.let{
            isFavorite = true
            checkFavoriteWord()
        }

        word.listMeans.let{ listMeans ->
            for (means in listMeans) {
                val kindView = TextView(requireContext()).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(
                            0,
                            10.dpToPx(requireContext()),
                            0,
                            0
                        )
                    }
                    text = means.kind
                    textSize = requireContext().resources.getDimension(R.dimen.small_title).pxToDp(requireContext())
                    setTextColor(requireContext().getColor(R.color.primary))
                    typeface = GlobalHelper(requireContext()).tinosBold
                }

                binding.wordMeans.addView(kindView)

                means.means.let{ listMean ->
                    for (mean in listMean) {
                        val wordMeanView = TextView(requireContext()).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(
                                    10.dpToPx(requireContext()),
                                    5.dpToPx(requireContext()),
                                    0,
                                    0
                                )
                            }
                            text = requireContext().getString(R.string.Word_mean_regex, mean.mean)
                            textSize = requireContext().resources.getDimension(R.dimen.medium_content).pxToDp(requireContext())
                            setTextColor(requireContext().getColor(R.color.secondPrimary))
                            typeface = GlobalHelper(requireContext()).tinosBold
                        }

                        binding.wordMeans.addView(wordMeanView)

                        mean.examples.let{ examples ->
                            if (examples.isNotEmpty()){
                                val exTitle = TextView(requireContext()).apply {
                                    layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    ).apply {
                                        setMargins(
                                            20.dpToPx(requireContext()),
                                            0,
                                            0,
                                            0
                                        )
                                    }
                                    text = requireContext().getString(R.string.Example_title)
                                    textSize = requireContext().resources.getDimension(R.dimen.small_content).pxToDp(requireContext())
                                    setTextColor(requireContext().getColor(R.color.black))
                                    typeface = GlobalHelper(requireContext()).tinosBold
                                }

                                binding.wordMeans.addView(exTitle)

                                for (ex in examples){
                                    val wordMeanExView = TextView(requireContext()).apply {
                                        layoutParams = LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                        ).apply {
                                            setMargins(
                                                20.dpToPx(requireContext()),
                                                0,
                                                0,
                                                0
                                            )
                                        }
                                        text = Html.fromHtml( requireContext().getString(
                                            R.string.Word_mean_example_regex,
                                            wordVM.getExEngContent(ex),
                                            wordVM.getExVieContent(ex)
                                        ), Html.FROM_HTML_MODE_LEGACY)
                                        textSize = requireContext().resources.getDimension(R.dimen.small_content).pxToDp(requireContext())
                                        setTextColor(requireContext().getColor(R.color.black))
                                        typeface = GlobalHelper(requireContext()).tinosItalic
                                    }

                                    binding.wordMeans.addView(wordMeanExView)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initListeners(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                handlePressedCallback()
            }
        })

        binding.icBack.setOnClickListener {
            handlePressedCallback()
        }

        binding.icFavorite.setOnClickListener {
            isFavorite = !isFavorite

            if (isFavorite){
                wordVM.addFavoriteWord(word)
                checkFavoriteWord()
            }
            else{
                wordVM.removeFavoriteWord(word)
                uncheckFavoriteWord()
            }
        }
    }

    private fun handlePressedCallback() {
        navController.popBackStack()
    }

    private fun initObserves(){
        wordVM.message.observe(viewLifecycleOwner){ message ->
            message?.let {
                requireContext().toastMessage(it)
                wordVM.message.value = null
            }
        }
    }

    private fun checkFavoriteWord() {
        binding.icFavorite.setImageDrawable(ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_favorite, null))
    }

    private fun uncheckFavoriteWord() {
        binding.icFavorite.setImageDrawable(ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_unfavorite, null))
    }
}