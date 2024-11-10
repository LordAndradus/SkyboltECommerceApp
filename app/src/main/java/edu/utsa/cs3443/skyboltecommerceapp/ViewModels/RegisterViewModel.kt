package edu.utsa.cs3443.skyboltecommerceapp.ViewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.utsa.cs3443.skyboltecommerceapp.Data.User
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.USER_COLLECTION
import edu.utsa.cs3443.skyboltecommerceapp.Util.RegisterFieldState
import edu.utsa.cs3443.skyboltecommerceapp.Util.RegistrationValidator
import edu.utsa.cs3443.skyboltecommerceapp.Util.Resource
import edu.utsa.cs3443.skyboltecommerceapp.Util.ValidateEmail
import edu.utsa.cs3443.skyboltecommerceapp.Util.ValidateFirstName
import edu.utsa.cs3443.skyboltecommerceapp.Util.ValidateLastName
import edu.utsa.cs3443.skyboltecommerceapp.Util.ValidatePassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * This controls the registration fragment and handles registering new users when clicking on the "REGISTER" button
 * Additionally, we will implement some Firebase back-end code.
 * Lastly, we are using Dagger Hilt as it makes the process of injecting code to ViewModels much more easier.
 */

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val FirebaseAuthenticator: FirebaseAuth,
    private val database: FirebaseFirestore
): ViewModel() {
    //We will be using registers here to keep track of how the app is operating, and to react accordingly
    private val _register = MutableStateFlow<Resource<User>>(Resource.Idle())
    val register: Flow<Resource<User>> = _register

    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()

    //Creates a new user and uploads that data to a Firebase database
    fun CreateAccountWithEmailAndPassword(user: User, password: String)
    {
        if(CheckValidation(user, password))
        {
            runBlocking {
                _register.emit(Resource.Loading())
            }

            FirebaseAuthenticator.createUserWithEmailAndPassword(user.Email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        SaveUserInformation(it.uid, user)
                        //_register.value = Resource.Success(it)
                    }
                }.addOnFailureListener{
                    _register.value = Resource.Error(it.message.toString())
                }
        }
        else
        {
            val rfs = RegisterFieldState(
                ValidateEmail(user.Email),
                ValidatePassword(password),
                ValidateFirstName(user.FirstName),
                ValidateLastName(user.LastName)
            )

            runBlocking {
                _validation.send(rfs)
            }
        }
    }

    private fun SaveUserInformation(userUID: String, user: User)
    {
        database.collection(USER_COLLECTION)
            .document(userUID)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }
            .addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
    }

    private fun CheckValidation(
        user: User,
        password: String
    ) : Boolean {
        val EmailValidated = ValidateEmail(user.Email)
        val PasswordValidated = ValidatePassword(password)
        val FirstNameValidated = ValidateFirstName(user.FirstName)
        val LastNameValidated = ValidateLastName(user.LastName)

        return EmailValidated is RegistrationValidator.Success
                && PasswordValidated is RegistrationValidator.Success
                && FirstNameValidated is RegistrationValidator.Success
                && LastNameValidated is RegistrationValidator.Success
    }
}