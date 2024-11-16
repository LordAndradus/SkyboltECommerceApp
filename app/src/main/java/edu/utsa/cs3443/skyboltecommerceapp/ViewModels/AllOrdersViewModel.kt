package edu.utsa.cs3443.skyboltecommerceapp.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.utsa.cs3443.skyboltecommerceapp.Data.Order
import edu.utsa.cs3443.skyboltecommerceapp.Helper.ResourceSignaler
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.ORDER_COLLECTION
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.USER_COLLECTION
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllOrdersViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authenticator: FirebaseAuth
): ViewModel() {

    val allOrders = ResourceSignaler<List<Order>>(this)

    init {
        getAllOrders()
    }

    fun getAllOrders()
    {
        allOrders.start()

        firestore.collection(USER_COLLECTION).document(authenticator.uid!!)
            .collection(ORDER_COLLECTION).get()
            .addOnSuccessListener {
                val orders = it.toObjects(Order::class.java)
                allOrders.success(orders)
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    allOrders.error(it.message.toString())
                }
            }
    }
}