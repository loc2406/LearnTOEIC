package com.locnguyen.toeicexercises.fragment.grammar

import android.app.Dialog
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.locnguyen.toeicexercises.databinding.GrammarFragmentBinding
import com.locnguyen.toeicexercises.databinding.ItemGrammarContentBinding
import com.locnguyen.toeicexercises.databinding.ItemGrammarExBinding
import com.locnguyen.toeicexercises.databinding.ItemGrammarFomulaBinding
import com.locnguyen.toeicexercises.databinding.ItemGrammarSubTitleBinding
import com.locnguyen.toeicexercises.model.Grammar
import com.locnguyen.toeicexercises.utils.DialogHelper

class GrammarFragment: Fragment() {
    private lateinit var binding: GrammarFragmentBinding
    private lateinit var navController: NavController
    private lateinit var grammar: Grammar

    private val args by navArgs<GrammarFragmentArgs>()
    private val loadingDialog: Dialog by lazy {DialogHelper.getLoadingDialog(requireActivity())}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GrammarFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        grammar = args.grammar

        initViews()
        initListeners()
        if (loadingDialog.isShowing) loadingDialog.dismiss()
    }

    private fun initViews() {
        binding.title.text = grammar.title

        grammar.contents.forEach { grammarSubContent ->
            grammarSubContent.subTitle.takeIf { it.isNotEmpty() }?.let{ title ->
                ItemGrammarSubTitleBinding.inflate(LayoutInflater.from(requireContext()), binding.contentSpace, true).apply {
                    root.text = title
                }
            }

            grammarSubContent.subContents.takeIf{ it.isNotEmpty()}?.let{ subContents ->
                subContents.forEach { grammarContent ->
                    grammarContent.content.takeIf { it.isNotEmpty() }?.let{ content ->
                        ItemGrammarContentBinding.inflate(LayoutInflater.from(requireContext()), binding.contentSpace, true).apply {
                            this.content.text = Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY)
                        }
                    }

                    grammarContent.formulas.takeIf { it.isNotEmpty() }?.let{ formulas ->
                        formulas.forEach { formula ->
                            ItemGrammarFomulaBinding.inflate(LayoutInflater.from(requireContext()), binding.contentSpace, true).apply {
                                root.text = formula
                            }
                        }
                    }

                    grammarContent.examples.takeIf { it.isNotEmpty() }?.let{ examples ->
                        examples.forEach { example ->
                            ItemGrammarExBinding.inflate(LayoutInflater.from(requireContext()), binding.contentSpace, true).apply {
                                this.example.text = Html.fromHtml(example.example, Html.FROM_HTML_MODE_LEGACY)
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