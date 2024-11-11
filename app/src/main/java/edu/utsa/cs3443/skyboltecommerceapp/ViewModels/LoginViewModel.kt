package edu.utsa.cs3443.skyboltecommerceapp.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.utsa.cs3443.skyboltecommerceapp.Util.LoginFieldState
import edu.utsa.cs3443.skyboltecommerceapp.Util.LoginValidator
import edu.utsa.cs3443.skyboltecommerceapp.Util.Resource
import edu.utsa.cs3443.skyboltecommerceapp.Util.ValidateLoginEmail
import edu.utsa.cs3443.skyboltecommerceapp.Util.ValidateLoginPassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private val TAG = "Login Fragment"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val FirebaseAuthenticator: FirebaseAuth
) : ViewModel() {
    private val _login = MutableSharedFlow<Resource<FirebaseUser>>()
    val login = _login.asSharedFlow()

    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

    private val _validation = Channel<LoginFieldState>()
    val validation = _validation.receiveAsFlow()

    /**
     *  Interacts with Firebase Authentication service to log user into application
     *
     *  Reports a success or failure, pending on if the user exists in the database
     *
     * @param String Email
     * @param String Password
     * @return null
     */
    fun Login(email: String, password: String)
    {
        if(CheckValidity(email, password))
        {
            runBlocking {
                _login.emit(Resource.Loading())
            }

            FirebaseAuthenticator.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        it.user?.let {
                            _login.emit(Resource.Success(it))
                        }
                    }
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _login.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
        else
        {
            val lfs = LoginFieldState(
                ValidateLoginEmail(email),
                ValidateLoginPassword(password)
            )

            runBlocking {
                _validation.send(lfs)
            }
        }

    }

    fun ResetPassword(Email: String)
    {
        viewModelScope.launch {
            _resetPassword.emit(Resource.Loading())
        }

        //Use firebase authentication to send link
        FirebaseAuthenticator.sendPasswordResetEmail(Email)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _resetPassword.emit(Resource.Success(Email))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _resetPassword.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun CheckValidity(email: String, password: String): Boolean
    {
        val ValidatedEmail = ValidateLoginEmail(email)
        val ValidatedPassword = ValidateLoginPassword(password)

        return ValidatedEmail is LoginValidator.success
                && ValidatedPassword is LoginValidator.success
    }
}