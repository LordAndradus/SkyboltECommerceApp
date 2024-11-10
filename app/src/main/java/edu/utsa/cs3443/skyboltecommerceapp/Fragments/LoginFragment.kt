package edu.utsa.cs3443.skyboltecommerceapp.Fragments.LoginRegister

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.utsa.cs3443.skyboltecommerceapp.Activities.ShoppingActivity
import edu.utsa.cs3443.skyboltecommerceapp.R
import edu.utsa.cs3443.skyboltecommerceapp.Util.Resource
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.LoginViewModel
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentLoginBinding

private val TAG: String = "Login Fragment"

@AndroidEntryPoint
class LoginFragment : Fragment(
    R.layout.fragment_login
){
    private lateinit var binding : FragmentLoginBinding
    private val _ViewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            binding.LoginButton.setOnClickListener {
                val email = EnterEmailHere.text.toString().trim()
                val password = EnterPasswordHere.text.toString()

                if(password == null || password.isEmpty())
                {
                    Toast.makeText(requireContext(), "Password cannot be empty!", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                if(email == null || email.isEmpty())
                {
                    Toast.makeText(requireContext(), "Email cannot be empty!", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                _ViewModel.Login(email, password)
            }

            binding.RegisterHere.setOnClickListener {
                Log.d(TAG, "User wants to register instead")
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            binding.ForgotLoginPassword.setOnClickListener {
                Log.d(TAG, "User wants to reset their password")
            }

            binding.GoogleLogin.setOnClickListener {
                Log.d(TAG, "User wants to login via Google")
            }

            binding.FacebookLogin.setOnClickListener {
                Log.d(TAG, "User wants to login via Facebook")

            }
        }

        lifecycleScope.launchWhenStarted {
            _ViewModel.login.collect() {
                when(it) {
                    is Resource.Loading -> {
                        binding.LoginButton.startAnimation()
                    }

                    is Resource.Success -> {
                        //revert animation to prevent memory leak
                        binding.LoginButton.revertAnimation()
                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                            //Pop LoginRegisterActivity from stack
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity((intent))
                        }

                    }

                    is Resource.Error -> {
                        binding.LoginButton.revertAnimation()
                        if(it.message == null || it.message.toString().isEmpty())
                        {
                            Toast.makeText(requireContext(), "Password cannot be empty!", Toast.LENGTH_LONG).show()
                            return@collect
                        }
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }

                    else -> Utilities.nop()
                }
            }
        }
    }
}