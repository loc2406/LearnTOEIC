package com.locnguyen.toeicexercises.fragment.exam

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.adapter.ExamAdapter
import com.locnguyen.toeicexercises.databinding.ListExamFragmentBinding
import com.locnguyen.toeicexercises.utils.DialogHelper
import com.locnguyen.toeicexercises.utils.toastMessage
import com.locnguyen.toeicexercises.viewmodel.ExamVM
import com.locnguyen.toeicexercises.viewmodel.main.MainVM

class ListExamFragment : Fragment() {

    private lateinit var binding: ListExamFragmentBinding
    private val examAdapter: ExamAdapter by lazy { ExamAdapter() }
    private val loadingDialog: Dialog by lazy {DialogHelper.getLoadingDialog(requireContext())}
    private val mainVM: MainVM by activityViewModels<MainVM>()
    private val examVM: ExamVM by activityViewModels<ExamVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListExamFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
        initObserves()
    }

    private fun initViews() {
        loadingDialog.show()
        examVM.getAllExamInFb()

        binding.listExam.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = examAdapter
        }
    }

    private fun initListeners() {
        examAdapter.itemClicked = { itemExam ->
            mainVM.itemExamClicked.value = itemExam
        }
    }

    private fun initObserves() {
        examVM.exams.observe(viewLifecycleOwner) { exams ->
            exams?.let {
                examAdapter.listExam = it
                examAdapter.notifyDataSetChanged()
            } ?: let{
                requireContext().toastMessage(R.string.Something_went_wrong_please_try_again)
            }
            loadingDialog.dismiss()
        }
    }
}