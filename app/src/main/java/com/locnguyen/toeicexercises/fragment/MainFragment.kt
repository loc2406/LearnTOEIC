package com.locnguyen.toeicexercises.fragment

import android.os.Bundle
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
import com.locnguyen.toeicexercises.adapter.BottomNavAdapter
import com.locnguyen.toeicexercises.databinding.MainFragmentBinding
import com.locnguyen.toeicexercises.utils.toastMessage
import com.locnguyen.toeicexercises.viewmodel.ExamVM
import com.locnguyen.toeicexercises.viewmodel.MainVM

class MainFragment: Fragment() {

    private lateinit var binding: MainFragmentBinding
    private lateinit var navController: NavController
    private lateinit var bottomNavAdapter: BottomNavAdapter
    private lateinit var mainVM: MainVM
    private lateinit var examVM: ExamVM

    private var backPressedTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainVM = ViewModelProvider(requireActivity())[MainVM::class.java]
        examVM = ViewModelProvider(requireActivity())[ExamVM::class.java]
        navController = Navigation.findNavController(view)

        initViews()
        initListeners()
        initObserves()
    }

    private fun initViews() {
        bottomNavAdapter = BottomNavAdapter(requireActivity())

        binding.fragmentViewPager2.apply {
            adapter = bottomNavAdapter
            offscreenPageLimit = 4
        }
    }

    private fun initListeners(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                handlePressedBack()
            }
        })

        binding.bottomNavMenu.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.bottom_nav_exercise -> binding.fragmentViewPager2.currentItem = 0
                R.id.bottom_nav_exam  -> binding.fragmentViewPager2.currentItem = 1
                R.id.bottom_nav_upgrade -> binding.fragmentViewPager2.currentItem = 2
                R.id.bottom_nav_setting -> binding.fragmentViewPager2.currentItem = 3
            }

            return@setOnItemSelectedListener true
        }

        binding.fragmentViewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                when(position){
                    0 -> binding.bottomNavMenu.menu.findItem(R.id.bottom_nav_exercise).setChecked(true)
                    1 -> binding.bottomNavMenu.menu.findItem(R.id.bottom_nav_exam).setChecked(true)
                    2 -> binding.bottomNavMenu.menu.findItem(R.id.bottom_nav_upgrade).setChecked(true)
                    3 -> binding.bottomNavMenu.menu.findItem(R.id.bottom_nav_setting).setChecked(true)
                }
            }
        })
    }

    private fun initObserves(){
        mainVM.itemTheoryClicked.observe(viewLifecycleOwner){ typeName ->
            typeName?.let{handleItemTheoryClicked(it)}
        }

        mainVM.itemExamClicked.observe(viewLifecycleOwner){ exam ->
            exam?.let {
                handleItemExamClicked()
                examVM.exam.value = exam
            }
        }
    }

    private fun handlePressedBack(){
        if (backPressedTime + 5000 > System.currentTimeMillis()) {
            requireActivity().onBackPressedDispatcher.onBackPressed()
            return
        } else {
            requireContext().toastMessage(R.string.Exit_app_message)
        }
        backPressedTime = System.currentTimeMillis()
    }

    private fun handleItemTheoryClicked(name: String) {
        when(name){
            "Từ vựng" -> {
                navController.navigate(R.id.action_mainFragment_to_wordFragment)
            }
            "Ngữ pháp" -> {
                navController.navigate(R.id.action_mainFragment_to_grammarFragment)
            }
        }
    }

    private fun handleItemExamClicked(){
        navController.navigate(R.id.action_mainFragment_to_detailFragment)
    }
}
