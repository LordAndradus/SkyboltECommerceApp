package edu.utsa.cs3443.skyboltecommerceapp.ViewModels

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.utsa.cs3443.skyboltecommerceapp.Data.User
import edu.utsa.cs3443.skyboltecommerceapp.SkyboltApplication
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.USER_COLLECTION
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.USER_PFP
import edu.utsa.cs3443.skyboltecommerceapp.Util.RegistrationValidator
import edu.utsa.cs3443.skyboltecommerceapp.Util.Resource
import edu.utsa.cs3443.skyboltecommerceapp.Util.ValidateEmail
import edu.utsa.cs3443.skyboltecommerceapp.Util.ValidateFirstName
import edu.utsa.cs3443.skyboltecommerceapp.Util.ValidateLastName
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authenticator: FirebaseAuth,
    private val storage: StorageReference,
    app: Application
): AndroidViewModel(app) {
    private val _user = MutableStateFlow<Resource<User>>(Resource.Idle())
    val user = _user.asStateFlow()

    private val _updateInfo = MutableStateFlow<Resource<User>>(Resource.Idle())
    val updateInfo = _updateInfo.asStateFlow()

    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

    init {
        getUser()
    }

    fun getUser()
    {
        viewModelScope.launch {
            _user.emit(Resource.Loading())
        }

        firestore.collection(USER_COLLECTION).document(authenticator.uid!!).get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                user?.let {
                    viewModelScope.launch {
                        _user.emit(Resource.Success(it))
                    }
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _user.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun updateUser(user: User, imageUri: Uri?)
    {
        val validUser = ValidateEmail(user.email) is RegistrationValidator.Success
                && ValidateFirstName(user.firstName) is RegistrationValidator.Success
                && ValidateLastName(user.lastName) is RegistrationValidator.Success

        if(!validUser)
        {
            viewModelScope.launch {
                _user.emit(Resource.Error("Check your inputs"))
            }

            return
        }

        viewModelScope.launch {
            _updateInfo.emit(Resource.Loading())
        }

        if(imageUri == null)
        {
            saveUserInformation(user, true)
        }
        else
        {
            saveUserInformationWithNewImage(user, imageUri)
        }
    }

    private fun saveUserInformation(user: User, shouldRetrieveOldImage: Boolean)
    {
        firestore.runTransaction { transaction ->
            val documentReference = firestore.collection(USER_COLLECTION).document(authenticator.uid!!)
            if(shouldRetrieveOldImage)
            {
                val currentUser = transaction.get(documentReference).toObject(User::class.java)
                val newUser = user.copy(imagePath = currentUser?.imagePath ?: "")
                transaction.set(documentReference, user)
            }
            else
            {
                transaction.set(documentReference, user)
            }
        }
            .addOnSuccessListener {
                viewModelScope.launch {
                    _updateInfo.emit(Resource.Success(user))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _updateInfo.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun saveUserInformationWithNewImage(user: User, imageUri: Uri)
    {
        viewModelScope.launch {
            try
            {
                val imageBitmap = MediaStore.Images.Media.getBitmap(
                    getApplication<SkyboltApplication>().contentResolver,
                    imageUri
                )

                val byteArrayOutputStream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 96, byteArrayOutputStream)
                val imageByteArray = byteArrayOutputStream.toByteArray()
                val imageDirectory = storage.child(USER_PFP + "/${authenticator.uid}/${UUID.randomUUID()}")
                val result = imageDirectory.putBytes(imageByteArray).await()
                val imageUrl = result.storage.downloadUrl.await().toString()
                saveUserInformation(user.copy(imagePath = imageUrl), false)
            }
            catch(e: Exception)
            {
                e.printStackTrace()
                _user.emit(Resource.Error(e.message.toString()))
            }
        }
    }

    fun ResetPassword(Email: String)
    {
        viewModelScope.launch {
            _resetPassword.emit(Resource.Loading())
        }

        //Use firebase authentication to send link
        authenticator.sendPasswordResetEmail(Email)
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
}