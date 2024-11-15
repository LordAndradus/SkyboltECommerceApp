package edu.utsa.cs3443.skyboltecommerceapp.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.utsa.cs3443.skyboltecommerceapp.Data.User
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.USER_COLLECTION
import edu.utsa.cs3443.skyboltecommerceapp.Util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authenticator: FirebaseAuth
): ViewModel() {
    private val _user = MutableStateFlow<Resource<User>>(Resource.Idle())
    val user = _user.asStateFlow()

    init {
        getUser()
    }

    fun getUser()
    {
        viewModelScope.launch {
            _user.emit(Resource.Loading())
        }

        firestore.collection(USER_COLLECTION).document(authenticator.uid!!)
            .addSnapshotListener { value, e ->
                if(e != null)
                {
                    viewModelScope.launch {
                        _user.emit(Resource.Error(e.message.toString()))
                    }
                }
                else
                {
                    val user = value?.toObject(User::class.java)
                    user?.let {
                        viewModelScope.launch {
                            _user.emit(Resource.Success(user))
                        }
                    }
                }
            }
    }
}