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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import edu.utsa.cs3443.skyboltecommerceapp.Activities.ShoppingActivity
import edu.utsa.cs3443.skyboltecommerceapp.Dialogs.setupBottomSheetDialog
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

            binding.ForgotLoginPassword.setOnClickListener {
                Log.d(TAG, "User wants to reset their password")
                setupBottomSheetDialog { Email ->
                    _ViewModel.ResetPassword(Email)
                }
            }

            binding.RegisterHere.setOnClickListener {
                Log.d(TAG, "User wants to register instead")
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            binding.GoogleLogin.setOnClickListener {
                Log.d(TAG, "User wants to login via Google")
            }

            binding.FacebookLogin.setOnClickListener {
                Log.d(TAG, "User wants to login via Facebook")

            }
        }

        //Lifecycle listener when logging in
        lifecycleScope.launchWhenStarted {
            _ViewModel.login.collect() {
                Utilities.ResourceOperation<FirebaseUser>(it,
                    {
                        binding.LoginButton.startAnimation()
                    },
                    {
                        //revert animation to prevent memory leak
                        binding.LoginButton.revertAnimation()
                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                            //Pop LoginRegisterActivity from stack
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity((intent))
                        }
                    },
                    {
                        binding.LoginButton.revertAnimation()
                        if(it.message == null || it.message.toString().isEmpty())
                        {
                            Toast.makeText(requireContext(), "Password cannot be empty!", Toast.LENGTH_LONG).show()
                            return@ResourceOperation
                        }
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    })
            }
        }

        //Lifecycle listener when resetting password
        lifecycleScope.launchWhenStarted {
            _ViewModel.resetPassword.collect {
                Utilities.ResourceOperation<String>(it,
                    {

                    },
                    {
                        Snackbar.make(requireView(), "Reset link was sent to your email", Snackbar.LENGTH_LONG).show()
                    },
                    {
                        Snackbar.make(requireView(), "ERROR: ${it.message}", Snackbar.LENGTH_LONG).show()
                    })
            }
        }
    }
}