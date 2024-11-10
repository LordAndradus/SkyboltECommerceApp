package edu.utsa.cs3443.skyboltecommerceapp.Fragments.LoginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import edu.utsa.cs3443.skyboltecommerceapp.Data.User
import edu.utsa.cs3443.skyboltecommerceapp.R
import edu.utsa.cs3443.skyboltecommerceapp.Util.RegistrationValidator
import edu.utsa.cs3443.skyboltecommerceapp.Util.Resource
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.RegisterViewModel
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Handles the fragment xml for registering users.
 *
 * Adds functionality to the various buttons and editor text fields
 */

private val TAG : String = "RegisterFragment"

@AndroidEntryPoint
class RegisterFragment : Fragment()
{
    //The binder takes the Register_Fragement.xml, this is the alternative to R.layout. Thanks dagger hilt!
    private lateinit var binding : FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater);
        return binding.root
    }

    /**
     * A function that create a new user when the fields are properly inputted.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        //Here we bind each View to a specific press listener. IE, the user taps that view, it'll do some special logic
        binding.apply{
            RegisterAccountNow.setOnClickListener{
                val user = User(
                    Utilities.input(EnterFirstName),
                    Utilities.input(EnterLastName),
                    Utilities.input(EnterEmail),
                )

                val password : String = Utilities.input(EnterPassword);

                viewModel.CreateAccountWithEmailAndPassword(user, password);
            }

            LoginExists.setOnClickListener{
                Log.d(TAG, "User wants to login instead")
            }

            GoogleLogin.setOnClickListener{
                Log.d(TAG, "User wants to login via Google")
            }

            FacebookLogin.setOnClickListener{
                Log.d(TAG, "User wants to login via Facebook")
            }
        }

        //This is to specifically play an animation based on the state of Firebase, it will update when it reports anything
        lifecycleScope.launchWhenStarted {
            viewModel.register.collect{
                when(it)
                {
                    is Resource.Loading -> {
                        binding.RegisterAccountNow.startAnimation();
                    }

                    is Resource.Success -> {
                        Log.d(TAG, it.data.toString())
                        binding.RegisterAccountNow.revertAnimation();
                    }

                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        binding.RegisterAccountNow.revertAnimation();
                    }

                    else -> Utilities.nop() //Do nothing essentially
                }
            }
        }

        //Here we validate each input passed inside, if it fails to validate, then we set the error flag in the EditText field with a message
        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect { validation ->
                if(validation.firstname is RegistrationValidator.Failed)
                {
                    withContext(Dispatchers.Main) {
                        binding.EnterFirstName.apply {
                            requestFocus()
                            error = validation.firstname.message
                        }
                    }
                }

                if(validation.lastname is RegistrationValidator.Failed)
                {
                    withContext(Dispatchers.Main) {
                        binding.EnterLastName.apply {
                            requestFocus()
                            error = validation.lastname.message
                        }
                    }
                }

                if(validation.email is RegistrationValidator.Failed)
                {
                    withContext(Dispatchers.Main) {
                        binding.EnterEmail.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }

                if(validation.password is RegistrationValidator.Failed)
                {
                    withContext(Dispatchers.Main) {
                        binding.EnterPassword.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
            }
        }
    }
}