package com.locnguyen.toeicexercises.fragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.databinding.LoginFragmentBinding
import com.locnguyen.toeicexercises.sharedpreference.MySharedPreference
import com.locnguyen.toeicexercises.utils.toastMessage
import com.locnguyen.toeicexercises.viewmodel.LoginVM

class LoginFragment : Fragment() {

    private lateinit var binding: LoginFragmentBinding
    private lateinit var navController: NavController

    private val loginVM: LoginVM by viewModels<LoginVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.loginVM = loginVM
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        initObserves()
    }

    private fun initObserves() {
        loginVM.errMessage.observe(viewLifecycleOwner) { message ->
            message?.let { binding.errMessage.text = it }
        }

        loginVM.registerClicked.observe(viewLifecycleOwner) { isClicked ->
            isClicked.takeIf { it == true }?.let {
                navController.navigate(R.id.action_loginFragment_to_registerFragment)
                loginVM.registerClicked.value = false
            }
        }

        loginVM.isValidInfo.observe(viewLifecycleOwner) { isValid ->
            isValid.takeIf { it == true }?.let { loginVM.getUserInFb() }
        }

        loginVM.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                requireContext().toastMessage(R.string.Login_successful)
                MySharedPreference.getInstance(requireActivity().application).setUser(it)
                MySharedPreference.getInstance(requireActivity().application).setIsLoggedIn(true)
                navController.navigate(R.id.action_loginFragment_to_mainFragment)
            }
        }
    }
}