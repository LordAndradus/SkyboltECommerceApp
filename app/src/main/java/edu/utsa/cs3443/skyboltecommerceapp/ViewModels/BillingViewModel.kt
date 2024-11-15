package edu.utsa.cs3443.skyboltecommerceapp.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.utsa.cs3443.skyboltecommerceapp.Data.Address
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.ADDRESS_SUBCOLLECTION
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.USER_COLLECTION
import edu.utsa.cs3443.skyboltecommerceapp.Util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authenticator: FirebaseAuth
): ViewModel() {

    private val _address = MutableStateFlow<Resource<List<Address>>>(Resource.Idle())
    val address = _address.asStateFlow()

    init {
        getUserAddresses()
    }

    fun getUserAddresses()
    {
        viewModelScope.launch {
            _address.emit(Resource.Loading())
        }

        firestore.collection(USER_COLLECTION).document(authenticator.uid!!)
            .collection(ADDRESS_SUBCOLLECTION)
            .addSnapshotListener { value, e ->
                if(e != null)
                {
                    viewModelScope.launch {
                        _address.emit(Resource.Error(e.message.toString()))
                    }

                    return@addSnapshotListener
                }

                val addresses = value?.toObjects(Address::class.java)
                viewModelScope.launch {
                    _address.emit(Resource.Success(addresses!!))
                }
            }
    }
}