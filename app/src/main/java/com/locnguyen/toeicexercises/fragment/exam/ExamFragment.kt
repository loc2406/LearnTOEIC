package com.locnguyen.toeicexercises.fragment.exam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.locnguyen.toeicexercises.adapter.ExamAdapter
import com.locnguyen.toeicexercises.databinding.ExamFragmentBinding
import com.locnguyen.toeicexercises.model.Exam
import com.locnguyen.toeicexercises.viewmodel.ExamVM
import com.locnguyen.toeicexercises.viewmodel.MainVM

class ExamFragment: Fragment() {

    private lateinit var binding: ExamFragmentBinding
    private lateinit var examAdapter: ExamAdapter
    private lateinit var mainVM: MainVM
    private lateinit var examVM: ExamVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExamFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainVM = ViewModelProvider(requireActivity())[MainVM::class.java]
        examVM = ViewModelProvider(requireActivity())[ExamVM::class.java]

        val listExamJSON = examVM.getListExamFromAsset(requireContext(), "exams.json")

        if (listExamJSON.isNotEmpty()){
            val listExamType = object : TypeToken<List<Exam>>(){}.type
            val listExam = try {
                Gson().fromJson(listExamJSON, listExamType) as List<Exam>
            }catch(e: JsonSyntaxException){
                emptyList()
            }

            examAdapter = ExamAdapter(listExam){ itemExam ->
                mainVM.itemExamClicked.value = itemExam
            }
        }

        initViews()
    }

    private fun initViews() {
        binding.listExam.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = examAdapter
        }
    }
}