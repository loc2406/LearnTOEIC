package com.locnguyen.toeicexercises.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.adapter.BottomNavAdapter
import com.locnguyen.toeicexercises.databinding.MainFragmentBinding
import com.locnguyen.toeicexercises.sharedpreference.MySharedPreference
import com.locnguyen.toeicexercises.utils.DialogHelper
import com.locnguyen.toeicexercises.utils.toastMessage
import com.locnguyen.toeicexercises.viewmodel.ExamVM
import com.locnguyen.toeicexercises.viewmodel.main.ExerciseVM
import com.locnguyen.toeicexercises.viewmodel.main.MainVM
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    private lateinit var navController: NavController
    private lateinit var bottomNavAdapter: BottomNavAdapter
    private val mainVM: MainVM by activityViewModels<MainVM>()
    private val examVM: ExamVM by activityViewModels<ExamVM>()
    private val exerciseVM: ExerciseVM by activityViewModels<ExerciseVM>()

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

        navController = Navigation.findNavController(view)

        exerciseVM.getAllExamInFb()

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

    private fun initListeners() {

        binding.bottomNavMenu.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_nav_exercise -> binding.fragmentViewPager2.currentItem = 0
                R.id.bottom_nav_exam -> binding.fragmentViewPager2.currentItem = 1
                R.id.bottom_nav_upgrade -> binding.fragmentViewPager2.currentItem = 2
                R.id.bottom_nav_setting -> binding.fragmentViewPager2.currentItem = 3
            }

            return@setOnItemSelectedListener true
        }

        binding.fragmentViewPager2.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                when (position) {
                    0 -> binding.bottomNavMenu.menu.findItem(R.id.bottom_nav_exercise)
                        .setChecked(true)

                    1 -> binding.bottomNavMenu.menu.findItem(R.id.bottom_nav_exam).setChecked(true)
                    2 -> binding.bottomNavMenu.menu.findItem(R.id.bottom_nav_upgrade)
                        .setChecked(true)

                    3 -> binding.bottomNavMenu.menu.findItem(R.id.bottom_nav_setting)
                        .setChecked(true)
                }
            }
        })
    }

    private fun initObserves() {
        mainVM.itemTheoryClicked.observe(viewLifecycleOwner) { typeName ->
            typeName?.getContentIfNotHandled()?.let { handleItemTheoryClicked(it) }
        }

        mainVM.itemExerciseClicked.observe(viewLifecycleOwner) { typeName ->
            typeName?.getContentIfNotHandled()?.let { handleItemExerciseClicked(it) }
        }

        mainVM.itemExamClicked.observe(viewLifecycleOwner) { exam ->
            exam?.let {
                handleItemExamClicked()
                examVM.exam.value = exam
            }
        }

        mainVM.wordsNoteClicked.observe(viewLifecycleOwner) { isClicked ->
            isClicked.takeIf { it == true }?.let { handleWordNoteClicked() }
        }

        mainVM.grammarsNoteClicked.observe(viewLifecycleOwner) { isClicked ->
            isClicked.takeIf { it == true }?.let { handleGrammarNoteClicked() }
        }

        mainVM.editInfoClicked.observe(viewLifecycleOwner){ isClicked ->
            isClicked.getContentIfNotHandled()?.let {
                handleEditUserInfoClicked()
            }
        }

        mainVM.logoutClicked.observe(viewLifecycleOwner){ isClicked ->
            isClicked.getContentIfNotHandled()?.let {
                handleLogoutClicked()
            }
        }
    }

    private fun handleEditUserInfoClicked() {
        navController.navigate(R.id.action_mainFragment_to_editUserInfoFragment)
    }

    private fun handleLogoutClicked() {
        DialogHelper(requireContext()).getLogOutDialog {
            lifecycleScope.launch {
                try {
                    mainVM.logOutFb()
                    MySharedPreference.getInstance(requireActivity().application).apply {
                        setIsLoggedIn(false)
                        setUser(null)
                    }
                    navController.navigate(R.id.action_mainFragment_to_loginFragment)
                } catch (e: Exception) {
                    requireContext().toastMessage(R.string.Something_went_wrong_please_try_again)
                }
            }
        }.show()
    }

    private fun handleItemTheoryClicked(type: String) {
        when (type) {
            "Từ vựng" -> {
                navController.navigate(R.id.action_mainFragment_to_listWordFragment)
            }

            "Ngữ pháp" -> {
                navController.navigate(R.id.action_mainFragment_to_listGrammarFragment)
            }
        }
    }

    private fun handleItemExerciseClicked(type: String) {
        when (type) {
            "Chọn đáp án" -> {
                val action = MainFragmentDirections.actionMainFragmentToExerciseFragment("Chọn đáp án")
                navController.navigate(action)
            }

            "Đọc hiểu" -> {
                val action = MainFragmentDirections.actionMainFragmentToExerciseFragment("Đọc hiểu")
                navController.navigate(action)
            }
        }
    }

    private fun handleItemExamClicked() {
        val action = MainFragmentDirections.actionMainFragmentToExamFragment()
        navController.navigate(action)
    }

    private fun handleWordNoteClicked() {
        navController.navigate(R.id.action_mainFragment_to_favoriteWordsFragment)
    }

    private fun handleGrammarNoteClicked() {
        navController.navigate(R.id.action_mainFragment_to_favoriteGrammarsFragment)
    }
}
