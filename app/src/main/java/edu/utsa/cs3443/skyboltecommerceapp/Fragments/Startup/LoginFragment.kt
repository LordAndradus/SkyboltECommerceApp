package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Startup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.utsa.cs3443.skyboltecommerceapp.Activities.ShoppingActivity
import edu.utsa.cs3443.skyboltecommerceapp.Dialogs.setupBottomSheetDialog
import edu.utsa.cs3443.skyboltecommerceapp.R
import edu.utsa.cs3443.skyboltecommerceapp.Util.LoginValidator
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.LoginViewModel
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext

/**
 * Handles the fragment xml for Logging in users.
 *
 * Adds functionality to the various buttons and editor text fields
 */

private val TAG: String = "Login Fragment"

@AndroidEntryPoint
class LoginFragment : Fragment(
    R.layout.fragment_login
){
    private lateinit var binding : FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    private val validation = Channel

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

                viewModel.login(email, password)
            }

            binding.ForgotLoginPassword.setOnClickListener {
                setupBottomSheetDialog { Email ->
                    viewModel.resetPassword(Email)
                }
            }

            binding.RegisterHere.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            binding.GoogleLogin.setOnClickListener {
                Log.d(TAG, "User wants to login via Google")
            }

            binding.FacebookLogin.setOnClickListener {
                Log.d(TAG, "User wants to login via Facebook")

            }
        }

        viewModel.login.handleScope(this, null,
            {binding.LoginButton.startAnimation()}, {
                binding.LoginButton.revertAnimation()
                Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                    //Pop LoginRegisterActivity from stack
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }, {binding.LoginButton.revertAnimation()})

        viewModel.login.onFailureIterator = {
            if(it.message == null || it.message.toString().isEmpty())
            {
                Utilities.showSnackbar(requireView(), "Password cannot be empty!")
            }
        }

        viewModel.resetPassword.handleScope(this, null, null,
            {Utilities.showSnackbar(requireView(), "Reset link was sent to your email")})

        viewModel.resetPassword.onFailureIterator = {
            Utilities.showSnackbar(requireView(), "ERROR: ${it.message}")
        }

        //Here we validate each input passed inside, if it fails to validate, then we set the error flag in the EditText field with a message
        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect { validation ->
                if(validation.email is LoginValidator.Failed)
                {
                    withContext(Dispatchers.Main) {
                        binding.EnterEmailHere.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }

                if(validation.password is LoginValidator.Failed)
                {
                    withContext(Dispatchers.Main) {
                        binding.EnterPasswordHere.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
            }
        }
    }
}