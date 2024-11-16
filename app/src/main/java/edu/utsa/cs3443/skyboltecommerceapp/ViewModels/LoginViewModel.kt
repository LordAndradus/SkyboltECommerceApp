package edu.utsa.cs3443.skyboltecommerceapp.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.utsa.cs3443.skyboltecommerceapp.Helper.ResourceSignaler
import edu.utsa.cs3443.skyboltecommerceapp.Util.LoginFieldState
import edu.utsa.cs3443.skyboltecommerceapp.Util.LoginValidator
import edu.utsa.cs3443.skyboltecommerceapp.Util.ValidateLoginEmail
import edu.utsa.cs3443.skyboltecommerceapp.Util.ValidateLoginPassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private val TAG = "Login Fragment"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticator: FirebaseAuth
) : ViewModel() {
    val login = ResourceSignaler<FirebaseUser>(this)
    val resetPassword = ResourceSignaler<String>(this)

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
    fun login(email: String, password: String)
    {
        if(checkValidity(email, password))
        {
            login.start()

            authenticator.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        it.user?.let {
                            login.success(it)
                        }
                    }
                }
                .addOnFailureListener {
                    login.error(it)
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

    fun resetPassword(Email: String)
    {
        resetPassword.start()

        //Use firebase authentication to send link
        authenticator.sendPasswordResetEmail(Email)
            .addOnSuccessListener {
                resetPassword.success(Email)
            }
            .addOnFailureListener {
                resetPassword.error(it)
            }
    }

    private fun checkValidity(email: String, password: String): Boolean
    {
        return ValidateLoginEmail(email) is LoginValidator.success
                && ValidateLoginPassword(password) is LoginValidator.success
    }
}