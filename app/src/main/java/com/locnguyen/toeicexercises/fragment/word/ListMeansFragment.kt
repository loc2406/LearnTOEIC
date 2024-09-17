package com.locnguyen.toeicexercises.fragment.word

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.ListMeansFragmentBinding
import com.locnguyen.toeicexercises.model.Word
import com.locnguyen.toeicexercises.utils.dpToPx
import com.locnguyen.toeicexercises.utils.pxToDp
import com.locnguyen.toeicexercises.viewmodel.WordVM

class ListMeansFragment: Fragment() {
    private lateinit var binding: ListMeansFragmentBinding
    private lateinit var word: Word
    private lateinit var wordVM: WordVM
    private lateinit var navController: NavController

    private val args by navArgs<ListMeansFragmentArgs>()
    private val tinosBold by lazy {ResourcesCompat.getFont(requireContext(), R.font.tinos_bold)}
    private val tinosItalic by lazy {ResourcesCompat.getFont(requireContext(), R.font.tinos_italic)}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListMeansFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        wordVM = ViewModelProvider(requireActivity())[WordVM::class.java]
        word = args.word

        initViews()
        initListeners()
    }

    private fun initViews() {
        binding.headerTitle.text = word.title

        word.listMeans?.let{ listMeans ->
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
                    typeface = tinosBold
                }

                binding.wordMeans.addView(kindView)

                means.means?.let{ listMean ->
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
                            typeface = tinosBold
                        }

                        binding.wordMeans.addView(wordMeanView)

                        mean.examples?.let{ examples ->
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
                                    typeface = tinosBold
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
                                        typeface = tinosItalic
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
        binding.icBack.setOnClickListener {
            navController.popBackStack()
        }
    }
}