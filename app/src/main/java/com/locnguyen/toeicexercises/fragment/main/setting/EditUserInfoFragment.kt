package com.locnguyen.toeicexercises.fragment.main.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.EditUserInfoFragmentBinding
import com.locnguyen.toeicexercises.utils.toastMessage
import com.locnguyen.toeicexercises.viewmodel.main.setting.EditUserInfoVM

class EditUserInfoFragment: Fragment() {

    private lateinit var binding: EditUserInfoFragmentBinding
    private lateinit var navController: NavController

    private val editUserInfoVM: EditUserInfoVM by viewModels<EditUserInfoVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EditUserInfoFragmentBinding.inflate(inflater, container, false)
        binding.editUserInfoVM = editUserInfoVM
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        initObserves()
    }

    private fun initObserves() {
        editUserInfoVM.backClicked.observe(viewLifecycleOwner){ eventClicked ->
            eventClicked?.getContentIfNotHandled()?.let {
                navController.popBackStack()
            }
        }

        editUserInfoVM.isUpdated.observe(viewLifecycleOwner){ eventUpdated ->
            eventUpdated?.getContentIfNotHandled()?.let { isUpdated ->
                if (isUpdated){
                    requireContext().toastMessage(requireContext().getString(R.string.Update_user_info_successful))
                }
                else{
                    requireContext().toastMessage(requireContext().getString(R.string.Update_user_info_failed))
                }
                navController.popBackStack()
            }
        }
    }
}