package edu.utsa.cs3443.skyboltecommerceapp.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.utsa.cs3443.skyboltecommerceapp.Data.Order
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.ORDER_COLLECTION
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.USER_COLLECTION
import edu.utsa.cs3443.skyboltecommerceapp.Util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllOrdersViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authenticator: FirebaseAuth
): ViewModel() {
    private val _allOrders = MutableStateFlow<Resource<List<Order>>>(Resource.Idle())
    val allOrders = _allOrders.asStateFlow()

    init {
        getAllOrders()
    }

    fun getAllOrders()
    {
        viewModelScope.launch {
            _allOrders.emit(Resource.Loading())
        }

        firestore.collection(USER_COLLECTION).document(authenticator.uid!!)
            .collection(ORDER_COLLECTION).get()
            .addOnSuccessListener {
                val orders = it.toObjects(Order::class.java)
                viewModelScope.launch {
                    _allOrders.emit(Resource.Success(orders))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _allOrders.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}