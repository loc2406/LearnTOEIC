package com.locnguyen.toeicexercises.fragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.RegisterFragmentBinding
import com.locnguyen.toeicexercises.sharedpreference.MySharedPreference
import com.locnguyen.toeicexercises.utils.toastMessage
import com.locnguyen.toeicexercises.viewmodel.RegisterVM

class RegisterFragment: Fragment(){

    private lateinit var binding: RegisterFragmentBinding
    private lateinit var navController: NavController

    private val registerVM: RegisterVM by activityViewModels<RegisterVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegisterFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.registerVM = registerVM
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        initObserves()
    }

    private fun initObserves() {
        registerVM.errMessage.observe(viewLifecycleOwner) { message ->
            if (message != null){
                binding.errMessage.text = message
            }
        }

        registerVM.isValidInfo.observe(viewLifecycleOwner){isValid ->
            if (isValid){
                registerVM.createNewUser()
            }
        }

        registerVM.user.observe(viewLifecycleOwner){ user ->
            user?.let{
                requireContext().toastMessage(R.string.Register_new_account_successful)
                MySharedPreference.getInstance(requireActivity().application).setUser(it)
                MySharedPreference.getInstance(requireActivity().application).setIsLoggedIn(true)
                navController.navigate(R.id.action_registerFragment_to_mainFragment)
                registerVM.clearAllLiveData()
            }
        }
    }
}